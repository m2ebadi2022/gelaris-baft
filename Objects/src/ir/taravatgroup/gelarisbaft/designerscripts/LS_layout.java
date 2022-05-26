package ir.taravatgroup.gelarisbaft.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_layout{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 3;BA.debugLine="ImageView1.VerticalCenter=30%y"[Layout/General script]
views.get("imageview1").vw.setTop((int)((30d / 100 * height) - (views.get("imageview1").vw.getHeight() / 2)));
//BA.debugLineNum = 4;BA.debugLine="ImageView1.HorizontalCenter=50%x"[Layout/General script]
views.get("imageview1").vw.setLeft((int)((50d / 100 * width) - (views.get("imageview1").vw.getWidth() / 2)));
//BA.debugLineNum = 5;BA.debugLine="Label2.HorizontalCenter=50%x"[Layout/General script]
views.get("label2").vw.setLeft((int)((50d / 100 * width) - (views.get("label2").vw.getWidth() / 2)));
//BA.debugLineNum = 6;BA.debugLine="Label2.Top=ImageView1.Bottom+10"[Layout/General script]
views.get("label2").vw.setTop((int)((views.get("imageview1").vw.getTop() + views.get("imageview1").vw.getHeight())+10d));
//BA.debugLineNum = 8;BA.debugLine="acount_icon.SetLeftAndRight(0,25%x)"[Layout/General script]
views.get("acount_icon").vw.setLeft((int)(0d));
views.get("acount_icon").vw.setWidth((int)((25d / 100 * width) - (0d)));
//BA.debugLineNum = 9;BA.debugLine="cart_icon.SetLeftAndRight(25%x,50%x)"[Layout/General script]
views.get("cart_icon").vw.setLeft((int)((25d / 100 * width)));
views.get("cart_icon").vw.setWidth((int)((50d / 100 * width) - ((25d / 100 * width))));
//BA.debugLineNum = 10;BA.debugLine="class_icon.SetLeftAndRight(50%x,75%x)"[Layout/General script]
views.get("class_icon").vw.setLeft((int)((50d / 100 * width)));
views.get("class_icon").vw.setWidth((int)((75d / 100 * width) - ((50d / 100 * width))));
//BA.debugLineNum = 11;BA.debugLine="shop_icon.SetLeftAndRight(75%x,100%x)"[Layout/General script]
views.get("shop_icon").vw.setLeft((int)((75d / 100 * width)));
views.get("shop_icon").vw.setWidth((int)((100d / 100 * width) - ((75d / 100 * width))));
//BA.debugLineNum = 13;BA.debugLine="lbl_acount.SetLeftAndRight(0,25%x)"[Layout/General script]
views.get("lbl_acount").vw.setLeft((int)(0d));
views.get("lbl_acount").vw.setWidth((int)((25d / 100 * width) - (0d)));
//BA.debugLineNum = 14;BA.debugLine="lbl_cart.SetLeftAndRight(25%x,50%x)"[Layout/General script]
views.get("lbl_cart").vw.setLeft((int)((25d / 100 * width)));
views.get("lbl_cart").vw.setWidth((int)((50d / 100 * width) - ((25d / 100 * width))));
//BA.debugLineNum = 15;BA.debugLine="lbl_class.SetLeftAndRight(50%x,75%x)"[Layout/General script]
views.get("lbl_class").vw.setLeft((int)((50d / 100 * width)));
views.get("lbl_class").vw.setWidth((int)((75d / 100 * width) - ((50d / 100 * width))));
//BA.debugLineNum = 16;BA.debugLine="lbl_shop.SetLeftAndRight(75%x,100%x)"[Layout/General script]
views.get("lbl_shop").vw.setLeft((int)((75d / 100 * width)));
views.get("lbl_shop").vw.setWidth((int)((100d / 100 * width) - ((75d / 100 * width))));
//BA.debugLineNum = 21;BA.debugLine="pan_menu.SetLeftAndRight(30%x,100%x)"[Layout/General script]
views.get("pan_menu").vw.setLeft((int)((30d / 100 * width)));
views.get("pan_menu").vw.setWidth((int)((100d / 100 * width) - ((30d / 100 * width))));
//BA.debugLineNum = 23;BA.debugLine="ImageView3.SetLeftAndRight(0,70%x)"[Layout/General script]
views.get("imageview3").vw.setLeft((int)(0d));
views.get("imageview3").vw.setWidth((int)((70d / 100 * width) - (0d)));
//BA.debugLineNum = 24;BA.debugLine="ImageView3.SetTopAndBottom(0,70%x)"[Layout/General script]
views.get("imageview3").vw.setTop((int)(0d));
views.get("imageview3").vw.setHeight((int)((70d / 100 * width) - (0d)));
//BA.debugLineNum = 26;BA.debugLine="ImageView2.HorizontalCenter=50%x"[Layout/General script]
views.get("imageview2").vw.setLeft((int)((50d / 100 * width) - (views.get("imageview2").vw.getWidth() / 2)));
//BA.debugLineNum = 28;BA.debugLine="scview_menu.Top=ImageView3.Bottom-100"[Layout/General script]
views.get("scview_menu").vw.setTop((int)((views.get("imageview3").vw.getTop() + views.get("imageview3").vw.getHeight())-100d));
//BA.debugLineNum = 29;BA.debugLine="scview_menu.Width=pan_menu.Width"[Layout/General script]
views.get("scview_menu").vw.setWidth((int)((views.get("pan_menu").vw.getWidth())));
//BA.debugLineNum = 32;BA.debugLine="loading.HorizontalCenter=WebView1.HorizontalCenter"[Layout/General script]
views.get("loading").vw.setLeft((int)((views.get("webview1").vw.getLeft() + views.get("webview1").vw.getWidth()/2) - (views.get("loading").vw.getWidth() / 2)));
//BA.debugLineNum = 33;BA.debugLine="loading.VerticalCenter=WebView1.VerticalCenter"[Layout/General script]
views.get("loading").vw.setTop((int)((views.get("webview1").vw.getTop() + views.get("webview1").vw.getHeight()/2) - (views.get("loading").vw.getHeight() / 2)));

}
}