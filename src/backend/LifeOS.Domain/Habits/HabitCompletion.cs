namespace LifeOS.Domain.Habits;

public sealed class HabitCompletion
{
    private HabitCompletion() { }

    public HabitCompletion(Guid id, Guid habitId, Guid userId, DateOnly localDate, string timeZoneId)
    {
        if (string.IsNullOrWhiteSpace(timeZoneId))
            throw new ArgumentException("An IANA time-zone identifier is required.", nameof(timeZoneId));
        Id = id;
        HabitId = habitId;
        UserId = userId;
        LocalDate = localDate;
        TimeZoneId = timeZoneId;
        CompletedAtUtc = DateTimeOffset.UtcNow;
    }

    public Guid Id { get; private set; }
    public Guid HabitId { get; private set; }
    public Guid UserId { get; private set; }
    public DateOnly LocalDate { get; private set; }
    public string TimeZoneId { get; private set; } = string.Empty;
    public DateTimeOffset CompletedAtUtc { get; private set; }
}