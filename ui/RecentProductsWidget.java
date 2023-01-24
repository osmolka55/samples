package qa.ok.projects.boohoo.ui.pages.components.recentproducts;

import javax.inject.Inject;

import qa.ok.fw.core.annotations.Step;
import qa.ok.fw.core.annotations.Sync;

import qa.ok.projects.boohoo.ui.pages.ShopWidget;
import qa.ok.projects.boohoo.ui.pages.plp.frg.PlpTileFragment;


public class RecentProductsWidget extends ShopWidget<RecentProductsWidgetUI> {

	@Inject
	public RecentProductsWidget() {
		super(RecentProductsWidgetUI.class);
	}


	/**
	 * Opens any Recommended Product if present.
	 *
	 * @return Product ID.
	 */
	@Step @Sync
	public String openFirstProduct() {

		PlpTileFragment productTile = ui.lstTiles
				.stream().findFirst()
				.orElseThrow(() -> new RuntimeException("Recent products are absent."));
		String pid = productTile.getPid();
		productTile.openPdpByProductImage();
		return pid;
	}
}
