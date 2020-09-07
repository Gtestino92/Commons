package com.commonsmodels.models;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FormatoGraph {
	private FormatoMaceta formato;
	private ColorGraph colorGraph;
	private List<Integer> values;
}
