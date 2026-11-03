using Microsoft.AspNetCore.Identity;

namespace LifeOS.Infrastructure.Identity;

public sealed class LifeOsUser : IdentityUser<Guid>
{
    public DateTimeOffset CreatedAt { get; set; } = DateTimeOffset.UtcNow;
    public string DisplayName { get; set; } = string.Empty;
}
