package br.gov.sibbr.api.model;

/**
 * This class should model the subset of occurrence fields that will be
 * delivered in the output for each occurrence record that matches the queries
 * 
 * @author Pedro Guimarães
 *
 */
public class Occurrence {
	public Integer auto_id;
	public Double decimallatitude;
	public Double decimallongitude;

	public Occurrence(Integer auto_id, Double decimallatitude, Double decimallongtude) {
		this.auto_id = auto_id;
		this.decimallatitude = decimallatitude;
		this.decimallongitude = decimallongtude;
	}

	public Integer getAuto_id() {
		return auto_id;
	}

	public void setAuto_id(Integer auto_id) {
		this.auto_id = auto_id;
	}

	public Double getDecimallatitude() {
		return decimallatitude;
	}

	public void setDecimallatitude(Double decimallatitude) {
		this.decimallatitude = decimallatitude;
	}

	public Double getDecimallongitude() {
		return decimallongitude;
	}

	public void setDecimallongitude(Double decimallongitude) {
		this.decimallongitude = decimallongitude;
	}
}
