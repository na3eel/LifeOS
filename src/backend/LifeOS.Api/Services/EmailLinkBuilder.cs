namespace LifeOS.Api.Services;

public static class EmailLinkBuilder
{
    public static string Build(IConfiguration configuration, string route, string email, string token)
    {
        var baseUrl = configuration["App:PublicBaseUrl"]
            ?? throw new InvalidOperationException("App:PublicBaseUrl must be configured.");
        var uri = new Uri(new Uri(baseUrl.TrimEnd('/') + "/"), route.TrimStart('/'));
        var query = $"email={Uri.EscapeDataString(email)}&token={Uri.EscapeDataString(token)}";
        return uri + "?" + query;
    }
}
