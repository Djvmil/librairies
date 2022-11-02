// Generated code from Butter Knife. Do not modify!
package com.kingfisher.sample;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity$TestAdapter$ViewHolder_ViewBinding implements Unbinder {
  private MainActivity.TestAdapter.ViewHolder target;

  @UiThread
  public MainActivity$TestAdapter$ViewHolder_ViewBinding(MainActivity.TestAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.textView = Utils.findRequiredViewAsType(source, R.id.tvName, "field 'textView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity.TestAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;
  }
}
