using LifeOS.Domain.Habits;
using LifeOS.Domain.LifeAreas;
using Microsoft.EntityFrameworkCore;

namespace LifeOS.Infrastructure.Persistence;

public sealed class LifeOsDbContext(DbContextOptions<LifeOsDbContext> options) : DbContext(options)
{
    public DbSet<Habit> Habits => Set<Habit>();
    public DbSet<HabitCompletion> HabitCompletions => Set<HabitCompletion>();
    public DbSet<LifeArea> LifeAreas => Set<LifeArea>();

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<LifeArea>(b =>
        {
            b.ToTable("life_areas");
            b.HasKey(x => x.Id);
            b.Property(x => x.Name).HasMaxLength(60).IsRequired();
            b.Property(x => x.Color).HasMaxLength(16).IsRequired();
            b.HasIndex(x => new { x.UserId, x.Name }).IsUnique();
            b.HasAlternateKey(x => new { x.UserId, x.Id });
        });

        modelBuilder.Entity<Habit>(b =>
        {
            b.ToTable("habits");
            b.HasKey(x => x.Id);
            b.Property(x => x.Name).HasMaxLength(120).IsRequired();
            b.Property(x => x.StartDate).HasColumnType("date");
            b.Property(x => x.EndDate).HasColumnType("date");
            b.HasIndex(x => new { x.UserId, x.IsActive });
            b.HasAlternateKey(x => new { x.UserId, x.Id });
            b.HasOne<LifeArea>()
                .WithMany()
                .HasForeignKey(x => new { x.UserId, x.LifeAreaId })
                .HasPrincipalKey(x => new { x.UserId, x.Id })
                .OnDelete(DeleteBehavior.Restrict);
        });

        modelBuilder.Entity<HabitCompletion>(b =>
        {
            b.ToTable("habit_completions");
            b.HasKey(x => x.Id);
            b.Property(x => x.LocalDate).HasColumnType("date");
            b.Property(x => x.TimeZoneId).HasMaxLength(100).IsRequired();
            b.HasIndex(x => new { x.UserId, x.HabitId, x.LocalDate }).IsUnique();
            b.HasIndex(x => new { x.UserId, x.LocalDate });
            b.HasOne<Habit>()
                .WithMany()
                .HasForeignKey(x => new { x.UserId, x.HabitId })
                .HasPrincipalKey(x => new { x.UserId, x.Id })
                .OnDelete(DeleteBehavior.Cascade);
        });
    }
}
