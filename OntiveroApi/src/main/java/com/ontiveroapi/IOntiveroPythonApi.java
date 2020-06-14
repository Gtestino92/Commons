package com.ontiveroapi;

import java.util.List;

import com.models.Maceta;
import com.models.Pedido;

public interface IOntiveroPythonApi {

	public List<Maceta> getRecomendaciones(Pedido pedidoSolicitado);
	
}
