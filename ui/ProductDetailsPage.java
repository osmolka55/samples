package qa.ok.projects.boohoo.ui.pages.pdp;

import javax.inject.Inject;
import javax.inject.Named;

import qa.ok.fw.core.annotations.Step;
import qa.ok.fw.core.annotations.Sync;
import qa.ok.fw.core.exceptions.NotSupportedByAUTException;

import qa.ok.projects.boohoo.ui.pages.ShopPage;
import qa.ok.projects.boohoo.ui.pages.components.recentproducts.RecentProductsWidget;
import qa.ok.projects.boohoo.ui.pages.components.recommendations.ProductRecommendationsWidget;
import qa.ok.projects.boohoo.ui.pages.footer.FooterWidget;
import qa.ok.projects.boohoo.ui.pages.pdp.widgets.ProductDetailsWidget;
import qa.ok.projects.boohoo.ui.pages.pdp.widgets.ProductImagesWidget;
import qa.ok.projects.boohoo.ui.pages.pdp.widgets.ProductNavigationWidget;
import qa.ok.projects.boohoo.ui.pages.pdp.widgets.ZoomWidget;

public class ProductDetailsPage extends ShopPage<ProductDetailsPageUI> {

	@Inject protected ProductImagesWidget productImages;
	@Inject protected ProductDetailsWidget productDetails;
	@SuppressWarnings("unused")
	@Inject protected ProductNavigationWidget productNavigationWidget;
	@Inject protected RecentProductsWidget recentlyViewedProducts;
	@Inject protected ProductRecommendationsWidget productRecommendations;
	@Inject protected ZoomWidget zoomOverlay;
	@Inject protected FooterWidget footerWidget;

	@Inject @Named("pdp.recommendations.title") String recommendationsTitle;


	@Inject
	public ProductDetailsPage() {
		super(ProductDetailsPageUI.class);
	}


	// region ==== Expose widgets ====


	@Sync public ProductImagesWidget images() { return productImages;}


	@Sync public ProductDetailsWidget details() { return productDetails;}


	@Sync public ProductNavigationWidget navigation() {
		//return productNavigationWidget;
		throw new NotSupportedByAUTException("Component is OOS for PWA Phase 1.");
	}


	@Sync public RecentProductsWidget recentlyViewed() { return recentlyViewedProducts;}


	@Sync
	public ProductRecommendationsWidget recommendations() {
		/*if (!productRecommendations.isOpened())
			browserSteps.loadLazyComponents();*/
		productRecommendations.scrollIntoCenter();
		return productRecommendations;
	}


	@Sync public ZoomWidget zoomOverlay() { return zoomOverlay;}


	@Sync
	public FooterWidget footer() {
		//if (!footerWidget.isOpened())
		//	browserSteps.loadLazyComponents(); // load footer
		return footerWidget;
	}
	// endregion ==== Expose widgets ====


	@Step @Sync
	public void verifyRecommendationsContent() {
		recommendations().verifyContent(recommendationsTitle);
	}
}
