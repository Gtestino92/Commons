package com.commonsmodels.models;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FormatoPredictGraph {
	private FormatoMaceta formato;
	private ColorGraph colorGraph;
	private BigDecimal value;
	private BigDecimal stdDev;
}
