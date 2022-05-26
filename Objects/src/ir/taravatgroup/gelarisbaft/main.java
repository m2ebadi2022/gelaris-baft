package ir.taravatgroup.gelarisbaft;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "ir.taravatgroup.gelarisbaft", "ir.taravatgroup.gelarisbaft.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "ir.taravatgroup.gelarisbaft", "ir.taravatgroup.gelarisbaft.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "ir.taravatgroup.gelarisbaft.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Timer _tim = null;
public static anywheresoftware.b4a.objects.Timer _tim_msg = null;
public ir.taravatgroup.gelarisbaft.b4xloadingindicator _loading = null;
public anywheresoftware.b4a.objects.WebViewWrapper _webview1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pan_menu = null;
public anywheresoftware.b4a.objects.PanelWrapper _pan_all_menu = null;
public anywheresoftware.b4a.phone.Phone _phon = null;
public anywheresoftware.b4a.objects.PanelWrapper _pan_notconn = null;
public static int _current_page = 0;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_shop_m = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_class_m = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_liked_m = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_info_m = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_account_m = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scview_menu = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_acount = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_cart = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_class = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_shop = null;
public anywheresoftware.b4a.objects.LabelWrapper _acount_icon = null;
public anywheresoftware.b4a.objects.LabelWrapper _cart_icon = null;
public anywheresoftware.b4a.objects.LabelWrapper _class_icon = null;
public anywheresoftware.b4a.objects.LabelWrapper _shop_icon = null;
public static String _str_shop = "";
public static String _str_class = "";
public static String _str_cart = "";
public static String _str_accont = "";
public static String _str_fav = "";
public static String _str_about = "";
public anywheresoftware.b4a.objects.LabelWrapper _lbl_title = null;
public static int _type_app = 0;
public ir.taravatgroup.gelarisbaft.httpjob _ht = null;
public anywheresoftware.b4a.phone.Phone _phon1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_share_app = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _img_app2 = null;
public static String _msg = "";
public anywheresoftware.b4a.objects.PanelWrapper _pan_notifi_all = null;
public anywheresoftware.b4a.objects.WebViewWrapper _web_msg_show = null;
public anywheresoftware.b4a.objects.collections.List _ls1 = null;
public anywheresoftware.b4a.objects.collections.List _ls2 = null;
public anywheresoftware.b4a.objects.collections.List _ls3 = null;
public static String _last_notif = "";
public anywheresoftware.b4a.objects.LabelWrapper _lbl_title_msgpan = null;
public anywheresoftware.b4a.objects.PanelWrapper _pan_notif = null;
public static boolean _is_now_instal = false;
public com.b4a.manamsoftware.PersianDate.ManamPersianDate _farsidate = null;
public static String _perdate1 = "";
public ir.taravatgroup.gelarisbaft.starter _starter = null;
public ir.taravatgroup.gelarisbaft.httputils2service _httputils2service = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _acount_icon_click() throws Exception{
 //BA.debugLineNum = 285;BA.debugLine="Private Sub acount_icon_Click";
 //BA.debugLineNum = 286;BA.debugLine="lbl_acount_Click";
_lbl_acount_click();
 //BA.debugLineNum = 287;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 100;BA.debugLine="Activity.LoadLayout(\"Layout\")";
mostCurrent._activity.LoadLayout("Layout",mostCurrent.activityBA);
 //BA.debugLineNum = 101;BA.debugLine="tim.Initialize(\"tim\",3000)";
_tim.Initialize(processBA,"tim",(long) (3000));
 //BA.debugLineNum = 102;BA.debugLine="tim.Enabled=True";
_tim.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 104;BA.debugLine="tim_msg.Initialize(\"tim_msg\",4000)";
_tim_msg.Initialize(processBA,"tim_msg",(long) (4000));
 //BA.debugLineNum = 107;BA.debugLine="perDate1=farsiDate.PersianLongDate";
mostCurrent._perdate1 = mostCurrent._farsidate.getPersianLongDate();
 //BA.debugLineNum = 110;BA.debugLine="WebView1.JavaScriptEnabled=True";
mostCurrent._webview1.setJavaScriptEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 114;BA.debugLine="scview_menu.Panel.LoadLayout(\"menu_item\")";
mostCurrent._scview_menu.getPanel().LoadLayout("menu_item",mostCurrent.activityBA);
 //BA.debugLineNum = 115;BA.debugLine="des_menu";
_des_menu();
 //BA.debugLineNum = 123;BA.debugLine="lbl_shop_Click";
_lbl_shop_click();
 //BA.debugLineNum = 124;BA.debugLine="lbl_title.Text=\"گلاریس بافت\"";
mostCurrent._lbl_title.setText(BA.ObjectToCharSequence("گلاریس بافت"));
 //BA.debugLineNum = 129;BA.debugLine="ls1.Initialize";
mostCurrent._ls1.Initialize();
 //BA.debugLineNum = 130;BA.debugLine="ls2.Initialize";
mostCurrent._ls2.Initialize();
 //BA.debugLineNum = 131;BA.debugLine="ls3.Initialize";
mostCurrent._ls3.Initialize();
 //BA.debugLineNum = 134;BA.debugLine="If(File.Exists(File.DirInternal,\"ls1\")) Then";
if ((anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"ls1"))) { 
 //BA.debugLineNum = 135;BA.debugLine="ls1=File.ReadList(File.DirInternal,\"ls1\")";
mostCurrent._ls1 = anywheresoftware.b4a.keywords.Common.File.ReadList(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"ls1");
 //BA.debugLineNum = 136;BA.debugLine="ls2=File.ReadList(File.DirInternal,\"ls2\")";
mostCurrent._ls2 = anywheresoftware.b4a.keywords.Common.File.ReadList(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"ls2");
 //BA.debugLineNum = 137;BA.debugLine="ls3=File.ReadList(File.DirInternal,\"ls3\")";
mostCurrent._ls3 = anywheresoftware.b4a.keywords.Common.File.ReadList(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"ls3");
 //BA.debugLineNum = 139;BA.debugLine="last_notif=ls1.Get(ls1.Size-1)";
mostCurrent._last_notif = BA.ObjectToString(mostCurrent._ls1.Get((int) (mostCurrent._ls1.getSize()-1)));
 //BA.debugLineNum = 140;BA.debugLine="is_now_instal=False";
_is_now_instal = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 143;BA.debugLine="http_initial_1(type_app)";
_http_initial_1(_type_app);
 //BA.debugLineNum = 145;BA.debugLine="loading.Hide";
mostCurrent._loading._hide /*String*/ ();
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _res1 = 0;
 //BA.debugLineNum = 482;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 483;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 484;BA.debugLine="If(pan_all_menu.Visible==True)Then";
if ((mostCurrent._pan_all_menu.getVisible()==anywheresoftware.b4a.keywords.Common.True)) { 
 //BA.debugLineNum = 485;BA.debugLine="pan_all_menu_Click";
_pan_all_menu_click();
 }else if((mostCurrent._pan_notifi_all.getVisible()==anywheresoftware.b4a.keywords.Common.True)) { 
 //BA.debugLineNum = 488;BA.debugLine="lbl_close_notif_Click";
_lbl_close_notif_click();
 }else if((_current_page!=1)) { 
 //BA.debugLineNum = 490;BA.debugLine="shop_icon_Click";
_shop_icon_click();
 }else if(((mostCurrent._webview1.getUrl()).equals(mostCurrent._str_shop) == false)) { 
 //BA.debugLineNum = 492;BA.debugLine="shop_icon_Click";
_shop_icon_click();
 }else {
 //BA.debugLineNum = 495;BA.debugLine="Dim res1 As Int";
_res1 = 0;
 //BA.debugLineNum = 496;BA.debugLine="res1 = Msgbox2(\"آیا قصد خروج دارید؟\", \"خروج\", \"";
_res1 = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("آیا قصد خروج دارید؟"),BA.ObjectToCharSequence("خروج"),"بله","","خیر",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 497;BA.debugLine="If res1 = DialogResponse.Positive Then";
if (_res1==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 498;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 };
 };
 //BA.debugLineNum = 503;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 505;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 507;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 260;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 262;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 256;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 258;BA.debugLine="End Sub";
return "";
}
public static String  _cart_icon_click() throws Exception{
 //BA.debugLineNum = 300;BA.debugLine="Private Sub cart_icon_Click";
 //BA.debugLineNum = 301;BA.debugLine="current_page=3";
_current_page = (int) (3);
 //BA.debugLineNum = 302;BA.debugLine="check_conn";
_check_conn();
 //BA.debugLineNum = 303;BA.debugLine="lbl_title.Text=\"سبد خرید\"";
mostCurrent._lbl_title.setText(BA.ObjectToCharSequence("سبد خرید"));
 //BA.debugLineNum = 304;BA.debugLine="WebView1.LoadUrl(str_cart)";
mostCurrent._webview1.LoadUrl(mostCurrent._str_cart);
 //BA.debugLineNum = 307;BA.debugLine="End Sub";
return "";
}
public static boolean  _check_conn() throws Exception{
String _color_act = "";
String _color_non = "";
boolean _connected = false;
 //BA.debugLineNum = 383;BA.debugLine="Sub check_conn As Boolean";
 //BA.debugLineNum = 384;BA.debugLine="Dim color_act As String =0xFF725BFF";
_color_act = BA.NumberToString(((int)0xff725bff));
 //BA.debugLineNum = 385;BA.debugLine="Dim color_non As String =0xFF292929";
_color_non = BA.NumberToString(((int)0xff292929));
 //BA.debugLineNum = 387;BA.debugLine="lbl_shop_m.Color=Colors.White";
mostCurrent._lbl_shop_m.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 388;BA.debugLine="lbl_class_m.Color=Colors.White";
mostCurrent._lbl_class_m.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 389;BA.debugLine="lbl_liked_m.Color=Colors.White";
mostCurrent._lbl_liked_m.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 390;BA.debugLine="lbl_info_m.Color=Colors.White";
mostCurrent._lbl_info_m.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 392;BA.debugLine="lbl_account_m.Color=Colors.White";
mostCurrent._lbl_account_m.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 394;BA.debugLine="lbl_shop_m.TextColor=color_non   'yes";
mostCurrent._lbl_shop_m.setTextColor((int)(Double.parseDouble(_color_non)));
 //BA.debugLineNum = 395;BA.debugLine="lbl_class_m.TextColor=color_non  ' no";
mostCurrent._lbl_class_m.setTextColor((int)(Double.parseDouble(_color_non)));
 //BA.debugLineNum = 396;BA.debugLine="lbl_liked_m.TextColor=color_non";
mostCurrent._lbl_liked_m.setTextColor((int)(Double.parseDouble(_color_non)));
 //BA.debugLineNum = 397;BA.debugLine="lbl_info_m.TextColor=color_non";
mostCurrent._lbl_info_m.setTextColor((int)(Double.parseDouble(_color_non)));
 //BA.debugLineNum = 399;BA.debugLine="lbl_account_m.TextColor=color_non";
mostCurrent._lbl_account_m.setTextColor((int)(Double.parseDouble(_color_non)));
 //BA.debugLineNum = 400;BA.debugLine="lbl_cart.TextColor=color_non";
mostCurrent._lbl_cart.setTextColor((int)(Double.parseDouble(_color_non)));
 //BA.debugLineNum = 401;BA.debugLine="lbl_class.TextColor=color_non";
mostCurrent._lbl_class.setTextColor((int)(Double.parseDouble(_color_non)));
 //BA.debugLineNum = 402;BA.debugLine="lbl_shop.TextColor=color_non";
mostCurrent._lbl_shop.setTextColor((int)(Double.parseDouble(_color_non)));
 //BA.debugLineNum = 403;BA.debugLine="acount_icon.TextColor=color_non";
mostCurrent._acount_icon.setTextColor((int)(Double.parseDouble(_color_non)));
 //BA.debugLineNum = 404;BA.debugLine="shop_icon.TextColor=color_non";
mostCurrent._shop_icon.setTextColor((int)(Double.parseDouble(_color_non)));
 //BA.debugLineNum = 405;BA.debugLine="class_icon.TextColor=color_non";
mostCurrent._class_icon.setTextColor((int)(Double.parseDouble(_color_non)));
 //BA.debugLineNum = 406;BA.debugLine="cart_icon.TextColor=color_non";
mostCurrent._cart_icon.setTextColor((int)(Double.parseDouble(_color_non)));
 //BA.debugLineNum = 407;BA.debugLine="lbl_acount.TextColor=color_non";
mostCurrent._lbl_acount.setTextColor((int)(Double.parseDouble(_color_non)));
 //BA.debugLineNum = 408;BA.debugLine="Select current_page";
switch (_current_page) {
case 1: {
 //BA.debugLineNum = 410;BA.debugLine="lbl_shop_m.TextColor=Colors.White";
mostCurrent._lbl_shop_m.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 411;BA.debugLine="lbl_shop.TextColor=color_act";
mostCurrent._lbl_shop.setTextColor((int)(Double.parseDouble(_color_act)));
 //BA.debugLineNum = 412;BA.debugLine="shop_icon.TextColor=color_act";
mostCurrent._shop_icon.setTextColor((int)(Double.parseDouble(_color_act)));
 //BA.debugLineNum = 413;BA.debugLine="lbl_shop_m.Color=color_act";
mostCurrent._lbl_shop_m.setColor((int)(Double.parseDouble(_color_act)));
 break; }
case 2: {
 //BA.debugLineNum = 415;BA.debugLine="lbl_class_m.TextColor=Colors.White";
mostCurrent._lbl_class_m.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 416;BA.debugLine="lbl_class_m.Color=color_act";
mostCurrent._lbl_class_m.setColor((int)(Double.parseDouble(_color_act)));
 //BA.debugLineNum = 417;BA.debugLine="lbl_class.TextColor=color_act";
mostCurrent._lbl_class.setTextColor((int)(Double.parseDouble(_color_act)));
 //BA.debugLineNum = 418;BA.debugLine="class_icon.TextColor=color_act";
mostCurrent._class_icon.setTextColor((int)(Double.parseDouble(_color_act)));
 break; }
case 3: {
 //BA.debugLineNum = 420;BA.debugLine="lbl_cart.TextColor=color_act";
mostCurrent._lbl_cart.setTextColor((int)(Double.parseDouble(_color_act)));
 //BA.debugLineNum = 421;BA.debugLine="cart_icon.TextColor=color_act";
mostCurrent._cart_icon.setTextColor((int)(Double.parseDouble(_color_act)));
 break; }
case 4: {
 //BA.debugLineNum = 423;BA.debugLine="lbl_account_m.TextColor=Colors.White";
mostCurrent._lbl_account_m.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 424;BA.debugLine="lbl_account_m.Color=color_act";
mostCurrent._lbl_account_m.setColor((int)(Double.parseDouble(_color_act)));
 //BA.debugLineNum = 425;BA.debugLine="lbl_acount.TextColor=color_act";
mostCurrent._lbl_acount.setTextColor((int)(Double.parseDouble(_color_act)));
 //BA.debugLineNum = 426;BA.debugLine="acount_icon.TextColor=color_act";
mostCurrent._acount_icon.setTextColor((int)(Double.parseDouble(_color_act)));
 break; }
case 5: {
 //BA.debugLineNum = 428;BA.debugLine="lbl_liked_m.TextColor=Colors.White";
mostCurrent._lbl_liked_m.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 429;BA.debugLine="lbl_liked_m.Color=color_act";
mostCurrent._lbl_liked_m.setColor((int)(Double.parseDouble(_color_act)));
 break; }
case 6: {
 //BA.debugLineNum = 431;BA.debugLine="lbl_info_m.TextColor=Colors.White";
mostCurrent._lbl_info_m.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 432;BA.debugLine="lbl_info_m.Color=color_act";
mostCurrent._lbl_info_m.setColor((int)(Double.parseDouble(_color_act)));
 break; }
case 10: {
 //BA.debugLineNum = 434;BA.debugLine="lbl_account_m.TextColor=Colors.White";
mostCurrent._lbl_account_m.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 435;BA.debugLine="lbl_account_m.Color=color_act";
mostCurrent._lbl_account_m.setColor((int)(Double.parseDouble(_color_act)));
 break; }
}
;
 //BA.debugLineNum = 441;BA.debugLine="Dim connected As Boolean =False";
_connected = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 442;BA.debugLine="If phon.GetDataState=\"CONNECTED\" Then";
if ((mostCurrent._phon.GetDataState()).equals("CONNECTED")) { 
 //BA.debugLineNum = 443;BA.debugLine="connected=True";
_connected = anywheresoftware.b4a.keywords.Common.True;
 }else if((mostCurrent._phon.GetSettings("wifi_on")).equals(BA.NumberToString(1))) { 
 //BA.debugLineNum = 445;BA.debugLine="connected=True";
_connected = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 447;BA.debugLine="If (connected=False)Then";
if ((_connected==anywheresoftware.b4a.keywords.Common.False)) { 
 //BA.debugLineNum = 448;BA.debugLine="pan_notconn.Visible=True";
mostCurrent._pan_notconn.setVisible(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 450;BA.debugLine="pan_notconn.Visible=False";
mostCurrent._pan_notconn.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 453;BA.debugLine="loading.Show";
mostCurrent._loading._show /*String*/ ();
 //BA.debugLineNum = 457;BA.debugLine="Return connected";
if (true) return _connected;
 //BA.debugLineNum = 460;BA.debugLine="End Sub";
return false;
}
public static String  _class_icon_click() throws Exception{
 //BA.debugLineNum = 309;BA.debugLine="Private Sub class_icon_Click";
 //BA.debugLineNum = 310;BA.debugLine="lbl_class_Click";
_lbl_class_click();
 //BA.debugLineNum = 311;BA.debugLine="End Sub";
return "";
}
public static String  _des_menu() throws Exception{
 //BA.debugLineNum = 241;BA.debugLine="Sub des_menu";
 //BA.debugLineNum = 242;BA.debugLine="lbl_shop_m.Width=scview_menu.Width";
mostCurrent._lbl_shop_m.setWidth(mostCurrent._scview_menu.getWidth());
 //BA.debugLineNum = 243;BA.debugLine="lbl_class_m.Width=scview_menu.Width";
mostCurrent._lbl_class_m.setWidth(mostCurrent._scview_menu.getWidth());
 //BA.debugLineNum = 244;BA.debugLine="lbl_liked_m.Width=scview_menu.Width";
mostCurrent._lbl_liked_m.setWidth(mostCurrent._scview_menu.getWidth());
 //BA.debugLineNum = 245;BA.debugLine="lbl_info_m.Width=scview_menu.Width";
mostCurrent._lbl_info_m.setWidth(mostCurrent._scview_menu.getWidth());
 //BA.debugLineNum = 247;BA.debugLine="lbl_account_m.Width=scview_menu.Width";
mostCurrent._lbl_account_m.setWidth(mostCurrent._scview_menu.getWidth());
 //BA.debugLineNum = 248;BA.debugLine="lbl_share_app.Width=scview_menu.Width";
mostCurrent._lbl_share_app.setWidth(mostCurrent._scview_menu.getWidth());
 //BA.debugLineNum = 253;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 26;BA.debugLine="Dim loading As B4XLoadingIndicator";
mostCurrent._loading = new ir.taravatgroup.gelarisbaft.b4xloadingindicator();
 //BA.debugLineNum = 29;BA.debugLine="Private WebView1 As WebView";
mostCurrent._webview1 = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private pan_menu As Panel";
mostCurrent._pan_menu = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private pan_all_menu As Panel";
mostCurrent._pan_all_menu = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim phon As Phone";
mostCurrent._phon = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 35;BA.debugLine="Private pan_notconn As Panel";
mostCurrent._pan_notconn = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Dim current_page As Int=1";
_current_page = (int) (1);
 //BA.debugLineNum = 38;BA.debugLine="Private lbl_shop_m As Label";
mostCurrent._lbl_shop_m = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private lbl_class_m As Label";
mostCurrent._lbl_class_m = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private lbl_liked_m As Label";
mostCurrent._lbl_liked_m = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private lbl_info_m As Label";
mostCurrent._lbl_info_m = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private lbl_account_m As Label";
mostCurrent._lbl_account_m = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private scview_menu As ScrollView";
mostCurrent._scview_menu = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private lbl_acount As Label";
mostCurrent._lbl_acount = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private lbl_cart As Label";
mostCurrent._lbl_cart = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private lbl_class As Label";
mostCurrent._lbl_class = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private lbl_shop As Label";
mostCurrent._lbl_shop = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private acount_icon As Label";
mostCurrent._acount_icon = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private cart_icon As Label";
mostCurrent._cart_icon = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private class_icon As Label";
mostCurrent._class_icon = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private shop_icon As Label";
mostCurrent._shop_icon = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Dim str_shop As String = \"https://gelaris-baft.ir";
mostCurrent._str_shop = "https://gelaris-baft.ir/shop/";
 //BA.debugLineNum = 58;BA.debugLine="Dim str_class As String = \"https://gelaris-baft.i";
mostCurrent._str_class = "https://gelaris-baft.ir/classy/";
 //BA.debugLineNum = 59;BA.debugLine="Dim str_cart As String = \"https://gelaris-baft.ir";
mostCurrent._str_cart = "https://gelaris-baft.ir/cart/";
 //BA.debugLineNum = 60;BA.debugLine="Dim str_accont As String = \"https://gelaris-baft.";
mostCurrent._str_accont = "https://gelaris-baft.ir/my-account/";
 //BA.debugLineNum = 61;BA.debugLine="Dim str_fav As String = \"https://gelaris-baft.ir/";
mostCurrent._str_fav = "https://gelaris-baft.ir/my-account/orders/";
 //BA.debugLineNum = 62;BA.debugLine="Dim str_about As String = \"https://gelaris-baft.i";
mostCurrent._str_about = "https://gelaris-baft.ir/about/";
 //BA.debugLineNum = 68;BA.debugLine="Private lbl_title As Label";
mostCurrent._lbl_title = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Dim type_app As Int=0";
_type_app = (int) (0);
 //BA.debugLineNum = 70;BA.debugLine="Dim ht As HttpJob";
mostCurrent._ht = new ir.taravatgroup.gelarisbaft.httpjob();
 //BA.debugLineNum = 71;BA.debugLine="Dim phon1 As Phone";
mostCurrent._phon1 = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 72;BA.debugLine="Private lbl_share_app As Label";
mostCurrent._lbl_share_app = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Private img_app2 As ImageView";
mostCurrent._img_app2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 76;BA.debugLine="Dim msg As String=\"\"";
mostCurrent._msg = "";
 //BA.debugLineNum = 78;BA.debugLine="Private pan_notifi_all As Panel";
mostCurrent._pan_notifi_all = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Private web_msg_show As WebView";
mostCurrent._web_msg_show = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Dim ls1 As List		'num msg";
mostCurrent._ls1 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 83;BA.debugLine="Dim ls2 As List		'msg";
mostCurrent._ls2 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 84;BA.debugLine="Dim ls3 As List		'date msg";
mostCurrent._ls3 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 86;BA.debugLine="Dim last_notif As String";
mostCurrent._last_notif = "";
 //BA.debugLineNum = 88;BA.debugLine="Private lbl_title_msgPan As Label";
mostCurrent._lbl_title_msgpan = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 89;BA.debugLine="Private pan_notif As Panel";
mostCurrent._pan_notif = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 91;BA.debugLine="Dim is_now_instal As Boolean=True";
_is_now_instal = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 93;BA.debugLine="Dim farsiDate As ManamPersianDate";
mostCurrent._farsidate = new com.b4a.manamsoftware.PersianDate.ManamPersianDate();
 //BA.debugLineNum = 94;BA.debugLine="Dim perDate1 As String";
mostCurrent._perdate1 = "";
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return "";
}
public static String  _http_initial_1(int _type1) throws Exception{
String _send = "";
 //BA.debugLineNum = 153;BA.debugLine="Sub http_initial_1(type1 As Int)";
 //BA.debugLineNum = 154;BA.debugLine="ht.Initialize(\"ht\",Me)";
mostCurrent._ht._initialize /*String*/ (processBA,"ht",main.getObject());
 //BA.debugLineNum = 155;BA.debugLine="Dim send As String";
_send = "";
 //BA.debugLineNum = 156;BA.debugLine="send = \"username=mahdi&password=1234&div_id=\"&pho";
_send = "username=mahdi&password=1234&div_id="+mostCurrent._phon1.GetSettings("android_id")+"&sdk_ver="+BA.NumberToString(mostCurrent._phon1.getSdkVersion())+"&oprator="+mostCurrent._phon1.GetNetworkOperatorName()+"&type_app="+BA.NumberToString(_type1)+"&div_model="+mostCurrent._phon1.getModel()+"&last_notif="+mostCurrent._last_notif+"&var=0&is_now_instal="+BA.ObjectToString(_is_now_instal);
 //BA.debugLineNum = 157;BA.debugLine="ht.PostString(\"https://azarfadak.com/gelaris-baft";
mostCurrent._ht._poststring /*String*/ ("https://azarfadak.com/gelaris-baft.php",_send);
 //BA.debugLineNum = 160;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(ir.taravatgroup.gelarisbaft.httpjob _job) throws Exception{
 //BA.debugLineNum = 163;BA.debugLine="Sub Jobdone (job As HttpJob)";
 //BA.debugLineNum = 164;BA.debugLine="If job.Success = True Then";
if (_job._success /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 165;BA.debugLine="If job.JobName=\"ht\" Then";
if ((_job._jobname /*String*/ ).equals("ht")) { 
 //BA.debugLineNum = 166;BA.debugLine="If(job.GetString <> \"\")Then";
if (((_job._getstring /*String*/ ()).equals("") == false)) { 
 //BA.debugLineNum = 167;BA.debugLine="msg=job.GetString";
mostCurrent._msg = _job._getstring /*String*/ ();
 //BA.debugLineNum = 168;BA.debugLine="tim_msg.Enabled=True";
_tim_msg.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 };
 }else {
 };
 //BA.debugLineNum = 176;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_account_m_click() throws Exception{
 //BA.debugLineNum = 375;BA.debugLine="Private Sub lbl_account_m_Click";
 //BA.debugLineNum = 376;BA.debugLine="current_page=4";
_current_page = (int) (4);
 //BA.debugLineNum = 377;BA.debugLine="check_conn";
_check_conn();
 //BA.debugLineNum = 378;BA.debugLine="lbl_title.Text=\"حساب کاربری\"";
mostCurrent._lbl_title.setText(BA.ObjectToCharSequence("حساب کاربری"));
 //BA.debugLineNum = 379;BA.debugLine="WebView1.LoadUrl(str_accont)";
mostCurrent._webview1.LoadUrl(mostCurrent._str_accont);
 //BA.debugLineNum = 380;BA.debugLine="pan_all_menu_Click";
_pan_all_menu_click();
 //BA.debugLineNum = 381;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_acount_click() throws Exception{
 //BA.debugLineNum = 289;BA.debugLine="Private Sub lbl_acount_Click";
 //BA.debugLineNum = 290;BA.debugLine="current_page=4";
_current_page = (int) (4);
 //BA.debugLineNum = 291;BA.debugLine="check_conn";
_check_conn();
 //BA.debugLineNum = 292;BA.debugLine="lbl_title.Text=\"حساب کاربری\"";
mostCurrent._lbl_title.setText(BA.ObjectToCharSequence("حساب کاربری"));
 //BA.debugLineNum = 293;BA.debugLine="WebView1.LoadUrl(str_accont)";
mostCurrent._webview1.LoadUrl(mostCurrent._str_accont);
 //BA.debugLineNum = 294;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_cart_click() throws Exception{
 //BA.debugLineNum = 296;BA.debugLine="Private Sub lbl_cart_Click";
 //BA.debugLineNum = 297;BA.debugLine="cart_icon_Click";
_cart_icon_click();
 //BA.debugLineNum = 298;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_class_click() throws Exception{
 //BA.debugLineNum = 313;BA.debugLine="Private Sub lbl_class_Click";
 //BA.debugLineNum = 314;BA.debugLine="current_page=2";
_current_page = (int) (2);
 //BA.debugLineNum = 315;BA.debugLine="check_conn";
_check_conn();
 //BA.debugLineNum = 316;BA.debugLine="lbl_title.Text=\"دسته بندی\"";
mostCurrent._lbl_title.setText(BA.ObjectToCharSequence("دسته بندی"));
 //BA.debugLineNum = 317;BA.debugLine="WebView1.LoadUrl(str_class)";
mostCurrent._webview1.LoadUrl(mostCurrent._str_class);
 //BA.debugLineNum = 318;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_class_m_click() throws Exception{
 //BA.debugLineNum = 364;BA.debugLine="Private Sub lbl_class_m_Click";
 //BA.debugLineNum = 365;BA.debugLine="class_icon_Click";
_class_icon_click();
 //BA.debugLineNum = 366;BA.debugLine="pan_all_menu_Click";
_pan_all_menu_click();
 //BA.debugLineNum = 367;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_close_notif_click() throws Exception{
 //BA.debugLineNum = 236;BA.debugLine="Private Sub lbl_close_notif_Click";
 //BA.debugLineNum = 237;BA.debugLine="pan_notifi_all.Visible=False";
mostCurrent._pan_notifi_all.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 238;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_info_m_click() throws Exception{
 //BA.debugLineNum = 348;BA.debugLine="Private Sub lbl_info_m_Click";
 //BA.debugLineNum = 349;BA.debugLine="current_page=6";
_current_page = (int) (6);
 //BA.debugLineNum = 350;BA.debugLine="check_conn";
_check_conn();
 //BA.debugLineNum = 351;BA.debugLine="lbl_title.Text=\"درباره ما\"";
mostCurrent._lbl_title.setText(BA.ObjectToCharSequence("درباره ما"));
 //BA.debugLineNum = 352;BA.debugLine="WebView1.LoadUrl(str_about)";
mostCurrent._webview1.LoadUrl(mostCurrent._str_about);
 //BA.debugLineNum = 353;BA.debugLine="pan_all_menu_Click";
_pan_all_menu_click();
 //BA.debugLineNum = 354;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_liked_m_click() throws Exception{
 //BA.debugLineNum = 356;BA.debugLine="Private Sub lbl_liked_m_Click";
 //BA.debugLineNum = 357;BA.debugLine="current_page=5";
_current_page = (int) (5);
 //BA.debugLineNum = 358;BA.debugLine="check_conn";
_check_conn();
 //BA.debugLineNum = 359;BA.debugLine="lbl_title.Text=\"سفارشات من\"";
mostCurrent._lbl_title.setText(BA.ObjectToCharSequence("سفارشات من"));
 //BA.debugLineNum = 360;BA.debugLine="WebView1.LoadUrl(str_fav)";
mostCurrent._webview1.LoadUrl(mostCurrent._str_fav);
 //BA.debugLineNum = 361;BA.debugLine="pan_all_menu_Click";
_pan_all_menu_click();
 //BA.debugLineNum = 362;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_menu_click() throws Exception{
 //BA.debugLineNum = 343;BA.debugLine="Private Sub lbl_menu_Click";
 //BA.debugLineNum = 344;BA.debugLine="pan_all_menu.Visible=True";
mostCurrent._pan_all_menu.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 345;BA.debugLine="pan_menu.SetLayout(30%x,0,70%x,100%y)";
mostCurrent._pan_menu.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 346;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_notefi_click() throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _html = null;
int _i = 0;
 //BA.debugLineNum = 207;BA.debugLine="Private Sub lbl_notefi_Click";
 //BA.debugLineNum = 208;BA.debugLine="lbl_title_msgPan.Text=\"پیام ها\"";
mostCurrent._lbl_title_msgpan.setText(BA.ObjectToCharSequence("پیام ها"));
 //BA.debugLineNum = 209;BA.debugLine="pan_notifi_all.Visible=True";
mostCurrent._pan_notifi_all.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 210;BA.debugLine="pan_notif.Top=10%y";
mostCurrent._pan_notif.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 211;BA.debugLine="pan_notif.Height=70%y";
mostCurrent._pan_notif.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (70),mostCurrent.activityBA));
 //BA.debugLineNum = 212;BA.debugLine="web_msg_show.Height=pan_notif.Height-60dip";
mostCurrent._web_msg_show.setHeight((int) (mostCurrent._pan_notif.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60))));
 //BA.debugLineNum = 215;BA.debugLine="Dim html As StringBuilder";
_html = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 216;BA.debugLine="html.Initialize";
_html.Initialize();
 //BA.debugLineNum = 218;BA.debugLine="If(File.Exists(File.DirInternal,\"ls1\")) Then";
if ((anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"ls1"))) { 
 //BA.debugLineNum = 220;BA.debugLine="html.Append(\"<!DOCTYPE html><html><meta charset=";
_html.Append("<!DOCTYPE html><html><meta charset='UTF-8'><body dir='rtl'>");
 //BA.debugLineNum = 221;BA.debugLine="For i = ls1.Size-1 To 0 Step -1";
{
final int step10 = -1;
final int limit10 = (int) (0);
_i = (int) (mostCurrent._ls1.getSize()-1) ;
for (;_i >= limit10 ;_i = _i + step10 ) {
 //BA.debugLineNum = 222;BA.debugLine="html.Append(\"<div style='background-color: #99f";
_html.Append("<div style='background-color: #99ffff; font-size: 13px;'>"+"کد پیام : "+BA.ObjectToString(mostCurrent._ls1.Get(_i))+" - مورخ : "+BA.ObjectToString(mostCurrent._ls3.Get(_i))+"</div><div style='background-color: #e6ffff; font-size: 16px;'><br>"+BA.ObjectToString(mostCurrent._ls2.Get(_i))).Append("<br></div><br>");
 }
};
 //BA.debugLineNum = 224;BA.debugLine="html.Append(\"</body></html>\")";
_html.Append("</body></html>");
 //BA.debugLineNum = 227;BA.debugLine="web_msg_show.LoadHtml(html.ToString)";
mostCurrent._web_msg_show.LoadHtml(_html.ToString());
 }else {
 //BA.debugLineNum = 230;BA.debugLine="web_msg_show.LoadHtml(\"<html><body dir='rtl'>لیس";
mostCurrent._web_msg_show.LoadHtml("<html><body dir='rtl'>لیست پیام خالی ... </body></html>");
 };
 //BA.debugLineNum = 234;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_re_conn_click() throws Exception{
 //BA.debugLineNum = 466;BA.debugLine="Private Sub lbl_re_conn_Click";
 //BA.debugLineNum = 467;BA.debugLine="ProgressDialogShow(\"بارگذاری ...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("بارگذاری ..."));
 //BA.debugLineNum = 468;BA.debugLine="check_conn";
_check_conn();
 //BA.debugLineNum = 469;BA.debugLine="http_initial_1(type_app)";
_http_initial_1(_type_app);
 //BA.debugLineNum = 470;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_share_app_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 509;BA.debugLine="Private Sub lbl_share_app_Click";
 //BA.debugLineNum = 511;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 512;BA.debugLine="i.Initialize(i.ACTION_SEND, \"\")";
_i.Initialize(_i.ACTION_SEND,"");
 //BA.debugLineNum = 513;BA.debugLine="i.SetType(\"text/plain\")";
_i.SetType("text/plain");
 //BA.debugLineNum = 514;BA.debugLine="i.PutExtra(\"android.intent.extra.TEXT\", \"https://";
_i.PutExtra("android.intent.extra.TEXT",(Object)("https://cafebazaar.ir/app/ir.taravatgroup.gelarisbaft"));
 //BA.debugLineNum = 515;BA.debugLine="i.WrapAsIntentChooser(\"Select\")";
_i.WrapAsIntentChooser("Select");
 //BA.debugLineNum = 516;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_i.getObject()));
 //BA.debugLineNum = 518;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_shop_click() throws Exception{
 //BA.debugLineNum = 320;BA.debugLine="Private Sub lbl_shop_Click";
 //BA.debugLineNum = 321;BA.debugLine="shop_icon_Click";
_shop_icon_click();
 //BA.debugLineNum = 322;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_shop_m_click() throws Exception{
 //BA.debugLineNum = 369;BA.debugLine="Private Sub lbl_shop_m_Click";
 //BA.debugLineNum = 370;BA.debugLine="shop_icon_Click";
_shop_icon_click();
 //BA.debugLineNum = 371;BA.debugLine="pan_all_menu_Click";
_pan_all_menu_click();
 //BA.debugLineNum = 372;BA.debugLine="End Sub";
return "";
}
public static String  _pan_all_menu_click() throws Exception{
 //BA.debugLineNum = 336;BA.debugLine="Private Sub pan_all_menu_Click";
 //BA.debugLineNum = 338;BA.debugLine="pan_menu.SetLayout(100%x,0,70%x,100%y)";
mostCurrent._pan_menu.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 339;BA.debugLine="pan_all_menu.Visible=False";
mostCurrent._pan_all_menu.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 341;BA.debugLine="End Sub";
return "";
}
public static String  _pan_menu_click() throws Exception{
 //BA.debugLineNum = 331;BA.debugLine="Private Sub pan_menu_Click";
 //BA.debugLineNum = 333;BA.debugLine="End Sub";
return "";
}
public static String  _pan_notconn_click() throws Exception{
 //BA.debugLineNum = 462;BA.debugLine="Private Sub pan_notconn_Click";
 //BA.debugLineNum = 464;BA.debugLine="End Sub";
return "";
}
public static String  _pan_notifi_all_click() throws Exception{
 //BA.debugLineNum = 521;BA.debugLine="Private Sub pan_notifi_all_Click";
 //BA.debugLineNum = 523;BA.debugLine="End Sub";
return "";
}
public static String  _panel2_click() throws Exception{
 //BA.debugLineNum = 474;BA.debugLine="Private Sub Panel2_Click";
 //BA.debugLineNum = 476;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
httputils2service._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 18;BA.debugLine="Dim tim As Timer";
_tim = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 19;BA.debugLine="Dim tim_msg As Timer";
_tim_msg = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _shop_icon_click() throws Exception{
 //BA.debugLineNum = 324;BA.debugLine="Private Sub shop_icon_Click";
 //BA.debugLineNum = 325;BA.debugLine="current_page=1";
_current_page = (int) (1);
 //BA.debugLineNum = 326;BA.debugLine="check_conn";
_check_conn();
 //BA.debugLineNum = 327;BA.debugLine="lbl_title.Text=\"گلاریس بافت\"";
mostCurrent._lbl_title.setText(BA.ObjectToCharSequence("گلاریس بافت"));
 //BA.debugLineNum = 328;BA.debugLine="WebView1.LoadUrl(str_shop)";
mostCurrent._webview1.LoadUrl(mostCurrent._str_shop);
 //BA.debugLineNum = 329;BA.debugLine="End Sub";
return "";
}
public static String  _tim_msg_tick() throws Exception{
String _msg_key = "";
String _msg_value = "";
 //BA.debugLineNum = 179;BA.debugLine="Sub tim_msg_Tick";
 //BA.debugLineNum = 181;BA.debugLine="Dim msg_key As String";
_msg_key = "";
 //BA.debugLineNum = 182;BA.debugLine="Dim msg_value As String";
_msg_value = "";
 //BA.debugLineNum = 184;BA.debugLine="msg_key=msg.SubString2(0,4)";
_msg_key = mostCurrent._msg.substring((int) (0),(int) (4));
 //BA.debugLineNum = 185;BA.debugLine="msg_value=msg.SubString(5)";
_msg_value = mostCurrent._msg.substring((int) (5));
 //BA.debugLineNum = 187;BA.debugLine="ls1.Add(msg_key)";
mostCurrent._ls1.Add((Object)(_msg_key));
 //BA.debugLineNum = 188;BA.debugLine="ls2.Add(msg_value)";
mostCurrent._ls2.Add((Object)(_msg_value));
 //BA.debugLineNum = 189;BA.debugLine="ls3.Add(perDate1)";
mostCurrent._ls3.Add((Object)(mostCurrent._perdate1));
 //BA.debugLineNum = 191;BA.debugLine="File.WriteList(File.DirInternal,\"ls1\",ls1)";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"ls1",mostCurrent._ls1);
 //BA.debugLineNum = 192;BA.debugLine="File.WriteList(File.DirInternal,\"ls2\",ls2)";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"ls2",mostCurrent._ls2);
 //BA.debugLineNum = 193;BA.debugLine="File.WriteList(File.DirInternal,\"ls3\",ls3)";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"ls3",mostCurrent._ls3);
 //BA.debugLineNum = 195;BA.debugLine="lbl_title_msgPan.Text=\"پیام جدید\"";
mostCurrent._lbl_title_msgpan.setText(BA.ObjectToCharSequence("پیام جدید"));
 //BA.debugLineNum = 196;BA.debugLine="pan_notifi_all.Visible=True";
mostCurrent._pan_notifi_all.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 198;BA.debugLine="pan_notif.Top=20%y";
mostCurrent._pan_notif.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 199;BA.debugLine="pan_notif.Height=50%y";
mostCurrent._pan_notif.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 200;BA.debugLine="web_msg_show.Height=pan_notif.Height-60dip";
mostCurrent._web_msg_show.setHeight((int) (mostCurrent._pan_notif.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60))));
 //BA.debugLineNum = 203;BA.debugLine="web_msg_show.LoadHtml(\"<!DOCTYPE html><html><meta";
mostCurrent._web_msg_show.LoadHtml("<!DOCTYPE html><html><meta charset='UTF-8'><body dir='rtl'><div style='background-color: #99ffff; font-size: 14px;'>"+"کد پیام : "+_msg_key+" - مورخ : "+mostCurrent._perdate1+"</div><div style='background-color: #e6ffff; font-size: 18px;'><br>"+_msg_value+"<br></div><br></body></html>");
 //BA.debugLineNum = 204;BA.debugLine="tim_msg.Enabled=False";
_tim_msg.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 205;BA.debugLine="End Sub";
return "";
}
public static String  _tim_tick() throws Exception{
int _res3 = 0;
 //BA.debugLineNum = 264;BA.debugLine="Sub tim_Tick";
 //BA.debugLineNum = 266;BA.debugLine="check_conn";
_check_conn();
 //BA.debugLineNum = 267;BA.debugLine="Panel1.Visible=False";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 268;BA.debugLine="tim.Enabled=False";
_tim.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 271;BA.debugLine="If (File.Exists(File.DirInternal,\"setstart2\")==Tr";
if ((anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"setstart2")==anywheresoftware.b4a.keywords.Common.True)) { 
 //BA.debugLineNum = 272;BA.debugLine="Dim res3 As Int";
_res3 = 0;
 //BA.debugLineNum = 273;BA.debugLine="res3 = Msgbox2(\"برای اجرا در مرورگر ، گوگل کروم";
_res3 = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("برای اجرا در مرورگر ، گوگل کروم را نصب کنید و بعد امتحان نمائید."),BA.ObjectToCharSequence("توجه!"),"خروج","","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"attention.png").getObject()),mostCurrent.activityBA);
 //BA.debugLineNum = 274;BA.debugLine="If res3 = DialogResponse.Positive Then";
if (_res3==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 275;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 }else {
 //BA.debugLineNum = 277;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 };
 };
 //BA.debugLineNum = 282;BA.debugLine="End Sub";
return "";
}
public static String  _webview1_pagefinished(String _url) throws Exception{
 //BA.debugLineNum = 148;BA.debugLine="Sub WebView1_PageFinished (Url As String)";
 //BA.debugLineNum = 150;BA.debugLine="loading.Hide";
mostCurrent._loading._hide /*String*/ ();
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
}
