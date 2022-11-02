// Generated code from Butter Knife. Do not modify!
package com.kingfisher.sample;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.kingfisher.easyviewindicator.AnyViewIndicator;
import com.kingfisher.easyviewindicator.RecyclerViewIndicator;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    target.horizontalIndicator = Utils.findRequiredViewAsType(source, R.id.recyclerViewIndicator, "field 'horizontalIndicator'", RecyclerViewIndicator.class);
    target.recyclerView2 = Utils.findRequiredViewAsType(source, R.id.recyclerView2, "field 'recyclerView2'", RecyclerView.class);
    target.verticalIndicator = Utils.findRequiredViewAsType(source, R.id.anyViewIndicator, "field 'verticalIndicator'", AnyViewIndicator.class);
    target.recyclerView3 = Utils.findRequiredViewAsType(source, R.id.recyclerView3, "field 'recyclerView3'", RecyclerView.class);
    target.gridIndicator = Utils.findRequiredViewAsType(source, R.id.anyViewIndicator2, "field 'gridIndicator'", AnyViewIndicator.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.horizontalIndicator = null;
    target.recyclerView2 = null;
    target.verticalIndicator = null;
    target.recyclerView3 = null;
    target.gridIndicator = null;
  }
}
