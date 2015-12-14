package com.n4systems.services.config;

public class ProofTestConfig {
	protected Boolean chartPeakArrows;
	protected Boolean chartPeakDots;
	protected Boolean chartPeakMarkers;
	protected Integer chartSizeX;
	protected Integer chartSizeY;

	public ProofTestConfig() {}

	public ProofTestConfig(ProofTestConfig other) {
		this.chartPeakArrows = other.chartPeakArrows;
		this.chartPeakDots = other.chartPeakDots;
		this.chartPeakMarkers = other.chartPeakMarkers;
		this.chartSizeX = other.chartSizeX;
		this.chartSizeY = other.chartSizeY;
	}

	public Boolean getChartPeakArrows() {
		return chartPeakArrows;
	}

	public Boolean getChartPeakDots() {
		return chartPeakDots;
	}

	public Boolean getChartPeakMarkers() {
		return chartPeakMarkers;
	}

	public Integer getChartSizeX() {
		return chartSizeX;
	}

	public Integer getChartSizeY() {
		return chartSizeY;
	}

	@Override
	public String toString() {
		return "\t\tchartPeakArrows: " + chartPeakArrows + '\n' +
				"\t\tchartPeakDots: " + chartPeakDots + '\n' +
				"\t\tchartPeakMarkers: " + chartPeakMarkers + '\n' +
				"\t\tchartSizeX: " + chartSizeX + '\n' +
				"\t\tchartSizeY: " + chartSizeY + '\n';
	}
}
