/*
 * Copyright (C) 2025-2026 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.photon.bootstrap5.demo.pub.page.icon;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonempty;
import com.helger.html.css.DefaultCSSClassProvider;
import com.helger.html.css.ICSSClassProvider;
import com.helger.photon.bootstrap5.demo.app.ui.AbstractAppWebPage;
import com.helger.photon.uicore.page.WebPageExecutionContext;

/**
 * Abstract icon page
 *
 * @author Philip Helger
 */
public abstract class AbstractPagePublicIcon extends AbstractAppWebPage
{
  protected static final ICSSClassProvider CSS_CLASS_ICON_CONTAINER = DefaultCSSClassProvider.create ("icon-container");

  public AbstractPagePublicIcon (@NonNull @Nonempty final String sID, @NonNull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  protected abstract void fillIcons (@NonNull final WebPageExecutionContext aWPEC);

  @Override
  protected final void fillContent (@NonNull final WebPageExecutionContext aWPEC)
  {
    fillIcons (aWPEC);
  }
}
