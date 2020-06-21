package com.macetasontivero.core.ontiveroapi;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.macetasontivero.core.commonsmodels.models.Maceta;
import com.macetasontivero.core.commonsmodels.models.Pedido;

public interface IOntiveroPythonApi {

	public List<Maceta> getRecomendaciones(Pedido pedidoSolicitado);

	public String getPedidosEntregadosML(MultipartFile filePedidosML);
}
