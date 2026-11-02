using System.Net;
using System.Net.Mail;

namespace LifeOS.Api.Services;

public sealed class SmtpEmailSender(IConfiguration configuration, ILogger<SmtpEmailSender> logger) : IEmailSender
{
    public async Task SendAsync(string recipient, string subject, string htmlBody, CancellationToken cancellationToken)
    {
        var host = configuration["Email:SmtpHost"];
        var from = configuration["Email:From"];
        if (string.IsNullOrWhiteSpace(host) || string.IsNullOrWhiteSpace(from))
        {
            if (configuration.GetValue<bool>("Email:LogOnly"))
            {
                logger.LogWarning("Development email to {Recipient}: {Subject}. Body: {Body}", recipient, subject, htmlBody);
                return;
            }
            throw new InvalidOperationException("Email SMTP settings are not configured.");
        }

        using var message = new MailMessage(from, recipient, subject, htmlBody) { IsBodyHtml = true };
        using var client = new SmtpClient(host, configuration.GetValue<int?>("Email:SmtpPort") ?? 587)
        {
            EnableSsl = configuration.GetValue("Email:EnableSsl", true),
            Credentials = new NetworkCredential(configuration["Email:Username"], configuration["Email:Password"])
        };
        cancellationToken.ThrowIfCancellationRequested();
        await client.SendMailAsync(message, cancellationToken);
    }
}
