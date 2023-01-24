package qa.ok.projects.vfc.eapi.steps.cart.endpoints;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import io.restassured.response.Response;

import qa.ok.fw.core.annotations.Step;

import qa.ok.projects.vfc.api.RequestData;
import qa.ok.projects.vfc.eapi.steps.BaseApiSteps;
import qa.ok.projects.vfc.eapi.support.EApiSession;

public class Cart_MoveToFavorites extends BaseApiSteps {

	private static final String ENDPOINT_POST = "v1/cart/{basketId}/moveToFavorites";

	@Inject @Named("cart/move-to-favorites/post") Provider<RequestData> requestDataProvider;


	@Step
	public RequestData prepareRequest(EApiSession session, String pid,
	                                  String saveForLaterId, String saveForLaterItemId) {
		return p_prepareRequest(session, pid, saveForLaterId, saveForLaterItemId);
	}


	@Step
	public Response sendRequest(EApiSession session, String pid,
	                            String saveForLaterListId, String saveForLaterItemId) {
		var requestData = p_prepareRequest(session, pid, saveForLaterListId, saveForLaterItemId);
		return apiExecutor.sendRequest(session, requestData);
	}


	protected RequestData p_prepareRequest(EApiSession session, String pid,
	                                       String saveForLaterListId, String saveForLaterItemId) {
		var requestData = requestDataProvider.get();
		requestData.setPathParameter("{basketId}", session.cartId());

		requestData.payload.addProperty("productId", pid);
		requestData.payload.addProperty("saveForLaterId", saveForLaterListId);
		requestData.payload.addProperty("itemId", saveForLaterItemId);
		requestData.payload.addProperty("consumerID", session.consumerId());
		requestData.payload.addProperty("productImageUrl", "https://www.test");
		return requestData;
	}
}