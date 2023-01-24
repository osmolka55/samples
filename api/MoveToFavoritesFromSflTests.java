package qa.ok.projects.vfc.tests.eapi.cart.move_to_favorites;

import javax.inject.Inject;
import javax.inject.Named;

import org.testng.annotations.Test;

import qa.ok.fw.core.annotations.Jira;
import qa.ok.fw.core.annotations.TestId;

import qa.ok.projects.vfc.eapi.steps.cart.endpoints.Cart_MoveToFavorites;
import qa.ok.projects.vfc.eapi.steps.wishlists.favorites.Consumer_FavoritesGet;
import qa.ok.projects.vfc.eapi.steps.wishlists.saveforlater.Consumer_SaveForLaterGet;
import qa.ok.projects.vfc.tests.ApiTests;

public class MoveToFavoritesFromSflTests extends ApiTests {

	/*
	 *   MoveToFavorites  works only with SaveForLater list (2021-08-02)
	 *   I.e. when we say "move" them SaveForLater list assumed as source part
	 */

	@Inject Cart_MoveToFavorites cart_moveToFavorites;
	@Inject Consumer_SaveForLaterGet consumer_saveForLaterGet;
	@Inject Consumer_FavoritesGet consumer_favoritesGet;

	@Inject @Named("product.variation.id") String pid;

	@Inject @Named("schema.wishlists.favorites.move-to-favorites-from-sfl") String jsonSchemaPath;

	@Test @TestId("API-0435") @Jira("ECOM15-11434")
	public void requestWithMandatoryHeadersOnly() {

		var session = eapiSteps.newGuestSessionWithEmptyCart();
		var cartResponse = eapi.cart.addItem(session, pid, 1);
		var sflResponse = eapi.cart.moveItemToSaveForLater(session, cartResponse, pid);
		String saveForLaterListId = sflResponse.listId;
		String saveForLaterItemId = sflResponse.id;

		var request = cart_moveToFavorites.prepareRequest(session, pid, saveForLaterListId, saveForLaterItemId);

		request.keepMandatoryHeadersOnly();
		var response = apiExecutor.sendRequest(session, request);

		av.response.verifyHttpStatusCode(response, 201);
	}

	@Test @TestId("API-0439") @Jira({"ECOM15-11434"})
	public void requestWithMandatoryFieldsOnly() {

		var session = eapiSteps.newGuestSessionWithEmptyCart();
		var cartResponse = eapi.cart.addItem(session, pid, 1);
		var sflResponse = eapi.cart.moveItemToSaveForLater(session, cartResponse, pid);
		String saveForLaterListId = sflResponse.listId;
		String saveForLaterItemId = sflResponse.id;

		var request = cart_moveToFavorites.prepareRequest(session, pid, saveForLaterListId, saveForLaterItemId);

		request.keepMandatoryPayloadFieldsOnly();
		var response = apiExecutor.sendRequest(session, request);

		av.response.verifyHttpStatusCode(response, 201);
	}

	@Test @TestId("API-0437") @Jira("ECOM15-11434")
	public void moveFromSflToFavoritesJsonSchema() {

		var session = eapiSteps.newGuestSessionWithEmptyCart();
		var cartResponse = eapi.cart.addItem(session, pid, 1);
		var sflResponse = eapi.cart.moveItemToSaveForLater(session, cartResponse, pid);
		String saveForLaterListId = sflResponse.listId;
		String saveForLaterItemId = sflResponse.id;

		var response = cart_moveToFavorites.sendRequest(session, pid, saveForLaterListId, saveForLaterItemId);

		av.response.verifyHttpStatusCode(response, 201);
		av.response.verifyJsonSchema(response, jsonSchemaPath);
	}

	@Test @TestId("API-0438") @Jira("ECOM15-11434")
	public void moveItemFromSaveForLaterToFavorites() {

		var session = eapiSteps.newGuestSessionWithEmptyCart();
		var cartResponse = eapi.cart.addItem(session, pid, 1);
		var sflResponse = eapi.cart.moveItemToSaveForLater(session, cartResponse, pid);
		String saveForLaterListId = sflResponse.listId;
		String saveForLaterItemId = sflResponse.id;

		cart_moveToFavorites.sendRequest(session, pid, saveForLaterListId, saveForLaterItemId);

		var favoritesGetResponse = consumer_favoritesGet.sendRequest(session);
		av.response.verifyFieldValue(favoritesGetResponse, "items[0].id", pid);

		var saveForLaterGetResponse = consumer_saveForLaterGet.sendRequest(session);
		av.response.verifyFieldValue(saveForLaterGetResponse, "saveForLater[0].count", 0);
		av.response.verifyFieldAbsent(saveForLaterGetResponse, "saveForLater[0].items");
	}
}