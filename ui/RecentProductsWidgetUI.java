package qa.ok.projects.boohoo.ui.pages.components.recentproducts;

import java.util.List;

import org.openqa.selenium.support.FindBy;

import qa.ok.fragments.factory.GuicePageUIFactory;

import qa.ok.projects.boohoo.ui.pages.ShopWidgetUI;
import qa.ok.projects.boohoo.ui.pages.plp.frg.PlpTileFragment;

@FindBy(css = ".b-recently-viewed")
public class RecentProductsWidgetUI extends ShopWidgetUI {

	public RecentProductsWidgetUI(GuicePageUIFactory factory) {
		super(factory);
	}

	@FindBy(css = ".b-product-tile")
	public List<PlpTileFragment> lstTiles;
}
