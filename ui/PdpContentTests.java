package qa.ok.projects.boohoo.tests.pdp;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import qa.ok.fw.core.annotations.TestId;
import qa.ok.ocapi.model.shop.documents.product.Product;
import qa.ok.ocapi.model.shop.documents.product.ProductExpansions;

import qa.ok.projects.shared.utils.sfcc.ISiteUtils;

import qa.ok.projects.boohoo.tests.ShopTests;
import qa.ok.projects.boohoo.ui.pages.dialogs.Dialogs;
import qa.ok.projects.boohoo.ui.pages.pdp.ProductDetailsPage;
import qa.ok.projects.boohoo.ui.pages.plp.ProductListingPage;

public class PdpContentTests extends ShopTests {

	@Inject ProductListingPage plp;
	@Inject ProductDetailsPage pdp;
	@Inject Dialogs dialogs;
	@Inject ISiteUtils siteUtils;

	@Inject @Named("test-0236.pdp.information.items") List<String> informationItems;
	@Inject @Named("product.variation.id") private String variationId;
	@Inject @Named("product.variation.secondary.id") private String secondaryVariationId;
	@Inject @Named("site_currency") private String siteCurrency;


	private Product masterProduct;        // for variationId
	private String colorTitle;
	private String sizeTitle;
	Product variationProduct;


	@BeforeClass
	public void retrieveProductData() {

		variationProduct = ocapiSteps.getProduct(variationId, ProductExpansions.VARIATIONS);

		if (variationProduct.isVariant()) {
			masterProduct = ocapiSteps.getMasterProduct(variationProduct);
			colorTitle = siteUtils.getProductColorShortTitle(variationProduct);
			sizeTitle = siteUtils.getProductSizeShortTitle(variationProduct);
		}
	}


	@Test
	@TestId("0027")  //removed from automation. It checks GUI
	public void selectedThumbnailIsHighlighted() {

		preconditionSteps.startApplication();

		shopNavigationSteps.openPdp();

		pdp.images().selectThumbnail(1);
		pdp.images().verifyThumbnailHighlighted(1);

		pdp.images().selectThumbnail(0);
		pdp.images().verifyThumbnailHighlighted(0);
	}


	@Test
	@TestId("UAMT-0028")
	public void pdpMustContainProductRatingAndReviews() {

		preconditionSteps.startApplication();
		shopNavigationSteps.openPdp();
		pdp.details().verifyRatingStarsShown();
		pdp.details().verifyReviewsSectionShown();
	}


	@Test
	@TestId("TRT-0029")
	public void userCanOpenZoomedImageForProduct() {

		preconditionSteps.startApplication();
		shopNavigationSteps.openPdp();

		pdp.images().openZoomWindow();
		pdp.zoomOverlay().verifyOpened();
	}


	@Test
	@TestId("BHT-0030")
	public void userCanSelectProductVariations() {

		preconditionSteps.startApplication();
		shopNavigationSteps.openPdp(masterProduct);
		//pdp.details().verifyProductId(masterId);

		// TODO: check that colorTitle, sizeTitle is not currently selected.
		// TODO: maybe we should change implementation to have more good test:
		// todo: add 2 diff variations of the same master to basket. variations must have different sizes AND colors
		pdp.details().selectColor(colorTitle);
		pdp.details().selectSize(sizeTitle);
		pdp.details().verifyProductOpened(variationProduct);
	}


	@Test
	@TestId("TRT-0033")
	public void userCanOpenRecentlyViewedProduct() {

		preconditionSteps.startApplication();
		shopNavigationSteps.openPdp();

		// second time in order to show product in recent views
		shopNavigationSteps.openPdp();
		String pid = pdp.recentlyViewed().openFirstProduct();
		pdp.details().verifyProductId(pid);
	}


	@Test
	@TestId("0034")
	public void userCanExpandOnlyOneInformationBlockAtTime() {

		preconditionSteps.startApplication();
		shopNavigationSteps.openPdp();

		pdp.details().verifyOnlyOneInformationBlockIsVisible();

		pdp.details().openInformationBlock(0);
		pdp.details().verifyOnlyOneInformationBlockIsVisible();

		pdp.details().openInformationBlock(1);
		pdp.details().verifyOnlyOneInformationBlockIsVisible();
	}


	@Test
	@TestId("TRT-0035")
	public void firstTabIsOpenedByDefault() {

		preconditionSteps.startApplication();
		shopNavigationSteps.openPdp();
		//pdp.details().verifyFirstAccordionIsExpanded();
		pdp.details().verifyFirstProductDetailsBlockIsOpened();
	}


	@Test
	@TestId("BHT-0039")
	public void userCanOpenSizeGuide() {

		preconditionSteps.startApplication();
		shopNavigationSteps.openPdp(secondaryVariationId);
		pdp.details().openSizeGuide();
		dialogs.sizeGuideDialog().verifyOpened();
	}


	@Test
	@TestId("TRT-0040")
	public void userCanCloseZoomWindowWithMouse() {

		preconditionSteps.startApplication();
		shopNavigationSteps.openPdp();

		pdp.images().openZoomWindow();
		pdp.zoomOverlay().close();
		pdp.zoomOverlay().verifyClosed();
	}


	@Test
	@TestId("TRT-0041")
	public void userCanCloseZoomWindowWithKeyboard() {

		preconditionSteps.startApplication();
		shopNavigationSteps.openPdp();

		pdp.images().openZoomWindow();
		pdp.zoomOverlay().discard();
		pdp.zoomOverlay().verifyClosed();
	}


	@Test
	@TestId("0042")
	public void priceOnPdpDependsOnCurrentCurrency() {

		preconditionSteps.startApplication();
		shopNavigationSteps.openPdp();
		pdp.details().verifyCurrency(siteCurrency);
	}


	@Test
	@TestId("0043")
	public void productNavigationOnPdp() {

		preconditionSteps.startApplication();
		shopNavigationSteps.openPlp();

		plp.productsGrid().getProductTile(1).openPdp();
		pdp.waitForLoaded();
		String initialUrl = browserSteps.getCurrentUrl();

		pdp.navigation().openNext();                    // next product
		browserSteps.verifyUrlWasChanged(initialUrl);


		pdp.navigation().openPrevious();                // return back to initial
		browserSteps.verifyUrlOpened(initialUrl, "Current pdp");

		pdp.navigation().openPrevious();                // prev product
		browserSteps.verifyUrlWasChanged(initialUrl);
	}


	@Test
	@TestId("UAMT-0236")
	public void pdpContainProductDnaAndShippingInfo() {

		preconditionSteps.startApplication();
		shopNavigationSteps.openPdp();
		pdp.details().verifyProductDetailsTitles(informationItems);
		pdp.details().verifyFirstProductDetailsBlockIsOpened();
	}


	@Test
	@TestId("UAMT-0237")
	public void pdpLoginPopupContent() {

		preconditionSteps.startApplication();
		shopNavigationSteps.openPdp(variationId);
		pdp.details().tryAddToWishlist();
		dialogs.loginRequestDialog().verifyContent();
	}
}
