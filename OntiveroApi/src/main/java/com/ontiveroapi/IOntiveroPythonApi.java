package com.ontiveroapi;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.models.Maceta;
import com.models.Pedido;

public interface IOntiveroPythonApi {

	public List<Maceta> getRecomendaciones(Pedido pedidoSolicitado);

	public String getPedidosEntregadosML(MultipartFile filePedidosML);
}
