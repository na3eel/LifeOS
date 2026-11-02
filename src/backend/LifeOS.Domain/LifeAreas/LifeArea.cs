namespace LifeOS.Domain.LifeAreas;

public sealed class LifeArea
{
    private LifeArea() { }

    public LifeArea(Guid id, Guid userId, string name, string color)
    {
        if (string.IsNullOrWhiteSpace(name) || name.Length > 60)
            throw new ArgumentException("Life area name must contain 1–60 characters.", nameof(name));
        Id = id;
        UserId = userId;
        Name = name.Trim();
        Color = color;
    }

    public Guid Id { get; private set; }
    public Guid UserId { get; private set; }
    public string Name { get; private set; } = string.Empty;
    public string Color { get; private set; } = "#8DAA82";
}