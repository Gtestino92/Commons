package com.ontiveroapi;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.commonsmodels.models.FormatoPredictGraph;
import com.commonsmodels.models.Maceta;
import com.commonsmodels.models.Pedido;
import com.commonsmodels.models.PedidosEntregadosGraph;

public interface IOntiveroPythonApi {

	public List<Maceta> getRecomendaciones(Pedido pedidoSolicitado);

	public String getPedidosEntregadosML(MultipartFile filePedidosML);

	public PedidosEntregadosGraph getPedidosEntregadosDB();

	public List<FormatoPredictGraph> getPrediccionesByFormato(Date fechaPredict, PedidosEntregadosGraph pedidos);

	public List<FormatoPredictGraph> getPrediccionesByFormatoMock(Date fechaPredict, PedidosEntregadosGraph pedidos);
}
