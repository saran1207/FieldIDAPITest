package com.n4systems.util.time;


import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class RateTimer {
	public static final int MILLIS_PER_HOUR = 3600000;
	public static final int MILLIS_PER_MIN = 60000;
	public static final int MILLIS_PER_SEC = 1000;

	private long count;
	private long splitCount;

	private Instant start;
	private Instant split;

	public RateTimer start() {
		count = 0;
		start = Instant.now();
		return this;
	}

	public RateTimer split() {
		if (!isStarted()) throw new IllegalStateException("Must call start() before split()");
		split = Instant.now();
		splitCount = count;
		return this;
	}

	public RateTimer unsplit() {
		if (!isStarted()) throw new IllegalStateException("Must call start() before unsplit()");
		if (!isSplit()) throw new IllegalStateException("Must call split() before unsplit()");
		split = null;
		splitCount = 0;
		return this;
	}

	public RateTimer increment(long inc) {
		count += inc;
		return this;
	}

	public RateTimer increment() {
		return increment(1L);
	}

	public long getCount() {
		return count;
	}

	public long getSplitCount() {
		return splitCount;
	}

	public long getSplitCountDiff() {
		return count - splitCount;
	}

	public boolean isStarted() {
		return start != null;
	}

	public boolean isSplit() {
		return split != null;
	}

	public Duration elapsedTotal() {
		if (!isStarted()) throw new IllegalStateException("Must call start() before elapsedTotal()");
		return Duration.between(start, Instant.now());
	}

	public long elapsedTotal(TimeUnit unit) {
		return unit.convert(elapsedTotal().toNanos(), TimeUnit.NANOSECONDS);
	}

	public Duration elapsedSplit() {
		if (!isStarted()) throw new IllegalStateException("Must call start() before elapsedSplit()");
		if (!isSplit()) throw new IllegalStateException("Must call split() before elapsedSplit()");
		return Duration.between(start, split);
	}

	public long elapsedSplit(TimeUnit unit) {
		return unit.convert(elapsedSplit().toNanos(), TimeUnit.NANOSECONDS);
	}

	public Duration elapsedSinceSplit() {
		if (!isStarted()) throw new IllegalStateException("Must call start() before elapsedSplit()");
		if (!isSplit()) throw new IllegalStateException("Must call split() before elapsedSplit()");
		return Duration.between(split, Instant.now());
	}

	public long elapsedSinceSplit(TimeUnit unit) {
		return unit.convert(elapsedSinceSplit().toNanos(), TimeUnit.NANOSECONDS);
	}

	public double rateAvg(TimeUnit unit) {
		return ((double) count) / elapsedTotal(unit);
	}

	public double paceAvg(TimeUnit unit) {
		return elapsedTotal(unit) / ((double) count);
	}

	public double rateSplit(TimeUnit unit) {
		return ((double) splitCount) / elapsedSplit(unit);
	}

	public double paceSplit(TimeUnit unit) {
		return elapsedSplit(unit) / ((double) splitCount);
	}

	public double rateSinceSplit(TimeUnit unit) {
		return ((double) getSplitCountDiff()) / elapsedSinceSplit(unit);
	}

	public double paceSinceSplit(TimeUnit unit) {
		return elapsedSinceSplit(unit) / ((double) getSplitCountDiff());
	}

	private String formatDuration(Duration duration) {
		long mill = duration.toMillis();
		long hours = (long) Math.floor((double) mill / MILLIS_PER_HOUR);
		mill -= hours * MILLIS_PER_HOUR;
		long min = (long) Math.floor((double) mill / MILLIS_PER_MIN);
		mill -= min * MILLIS_PER_MIN;
		long sec = (long) Math.floor((double) mill / MILLIS_PER_SEC);
		mill -= sec * MILLIS_PER_SEC;
		return String.format("%02d:%02d:%02d.%d", hours, min, sec, mill);
	}

	public String elapsedString() {
		return formatDuration(elapsedTotal());
	}

	public String elapsedSplitString() {
		return formatDuration(elapsedSplit());
	}

	public String elapsedSinceSplitString() {
		return formatDuration(elapsedSinceSplit());
	}

	@Override
	public String toString() {
		return isStarted() ? String.format("Progress: [%d], Elapsed: [%s s], Rate: [%.2f units/s], Pace: [%.2f s/unit]", count, elapsedTotal(TimeUnit.SECONDS), rateAvg(TimeUnit.SECONDS), paceAvg(TimeUnit.SECONDS)) : "Not started";
	}

}
