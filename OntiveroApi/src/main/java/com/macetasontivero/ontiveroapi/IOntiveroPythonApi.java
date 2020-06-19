package com.macetasontivero.ontiveroapi;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.macetasontivero.models.Maceta;
import com.macetasontivero.models.Pedido;

public interface IOntiveroPythonApi {

	public List<Maceta> getRecomendaciones(Pedido pedidoSolicitado);

	public String getPedidosEntregadosML(MultipartFile filePedidosML);
}
