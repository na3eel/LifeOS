namespace LifeOS.Domain.Habits;

public sealed class Habit
{
    private Habit() { }

    public Habit(Guid id, Guid userId, string name, Guid lifeAreaId, DateOnly startDate)
    {
        if (string.IsNullOrWhiteSpace(name))
            throw new ArgumentException("Habit name is required.", nameof(name));
        if (name.Length > 120)
            throw new ArgumentOutOfRangeException(nameof(name), "Habit name cannot exceed 120 characters.");

        Id = id;
        UserId = userId;
        Name = name.Trim();
        LifeAreaId = lifeAreaId;
        StartDate = startDate;
        IsActive = true;
        CreatedAtUtc = DateTimeOffset.UtcNow;
    }

    public Guid Id { get; private set; }
    public Guid UserId { get; private set; }
    public string Name { get; private set; } = string.Empty;
    public Guid LifeAreaId { get; private set; }
    public DateOnly StartDate { get; private set; }
    public DateOnly? EndDate { get; private set; }
    public bool IsActive { get; private set; }
    public DateTimeOffset CreatedAtUtc { get; private set; }

    public void Rename(string name)
    {
        if (string.IsNullOrWhiteSpace(name) || name.Length > 120)
            throw new ArgumentException("Habit name must contain 1–120 characters.", nameof(name));
        Name = name.Trim();
    }

    public void Archive(DateOnly effectiveDate)
    {
        if (!IsActive) return;
        IsActive = false;
        EndDate = effectiveDate;
    }
}