package com.mattmerr.dr9k;

import org.jetbrains.annotations.Contract;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Punishment {
  private final int severityLevel;
  private final Instant punishmentStart;
  private final int decaySeconds = 30; //21600 for 6 hours

  public Punishment(int level, Instant startTime) {
    this.severityLevel = level;
    this.punishmentStart = startTime;
  }

  public String getHumanTimeRemaining() {
    Instant now = Instant.now();
    Duration timePassed = Duration.between(punishmentStart, now);
    long seconds = ((long) Math.pow(2, severityLevel));
    seconds -= timePassed.getSeconds();
    if (seconds <= 0) {
      return "none";
    }

    long years = seconds/ChronoUnit.YEARS.getDuration().getSeconds();
    seconds %= ChronoUnit.YEARS.getDuration().getSeconds();
    long months = seconds/ChronoUnit.MONTHS.getDuration().getSeconds();
    seconds %= ChronoUnit.MONTHS.getDuration().getSeconds();
    long weeks = seconds/ChronoUnit.WEEKS.getDuration().getSeconds();
    seconds %= ChronoUnit.WEEKS.getDuration().getSeconds();
    long days = seconds/ChronoUnit.DAYS.getDuration().getSeconds();
    seconds %= ChronoUnit.DAYS.getDuration().getSeconds();
    long hours = seconds/ChronoUnit.HOURS.getDuration().getSeconds();
    seconds %= ChronoUnit.HOURS.getDuration().getSeconds();
    long minutes = seconds/ChronoUnit.MINUTES.getDuration().getSeconds();
    seconds %= ChronoUnit.MINUTES.getDuration().getSeconds();
    String ret = ""; //TODO: swtich to stringbuilder" "
    if (years > 0) {
      ret += String.format("%d years, ", years);
    }
    if (months > 0) {
      ret += String.format("%d months, ", months);
    }
    if (weeks > 0) {
      ret += String.format("%d weeks, ", weeks);
    }
    if (days > 0) {
      ret += String.format("%d days, ", days);
    }
    if (hours > 0) {
      ret += String.format("%d hours, ", hours);
    }
    if (minutes > 0) {
      ret += String.format("%d minutes, ", minutes);
    }
    if (seconds > 0) {
      ret += String.format("%d seconds", seconds);
    }
    return ret;
  }

  public boolean isOver() {
    Instant now = Instant.now();
    Duration timePassed = Duration.between(punishmentStart, now);
    long seconds = ((long) Math.pow(2, severityLevel));
    seconds -= timePassed.getSeconds();
    if (seconds <= 0) {
      return true;
    }
    return false;
  }

  public boolean getPunishmentDecayed() {
    int decay = getPunishmentDecay();

    return severityLevel <= decay;
  }

  public int getPunishmentDecay() {
    Instant now = Instant.now();
    Duration timePassed = Duration.between(punishmentStart, now);
    long secondsPassed = timePassed.getSeconds();
    return (int) secondsPassed/decaySeconds;
  }

  public int getSeverityLevel() {
    return severityLevel;
  }

  public Instant getPunishmentStart() {
    return punishmentStart;
  }

  @Contract(value = "null -> false", pure = true)
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (getClass() != o.getClass()) {
      return false;
    }
    Punishment e = (Punishment) o;
    return Objects.equals(severityLevel, e.severityLevel) && Objects
        .equals(punishmentStart, e.punishmentStart);
  }

  @Override
  public int hashCode() {
    return Objects.hash(severityLevel, punishmentStart);
  }

  @Override
  public String toString() {
    return String.format("Severtiy %d: Start %s", severityLevel,
        punishmentStart.toString());
  }
}
