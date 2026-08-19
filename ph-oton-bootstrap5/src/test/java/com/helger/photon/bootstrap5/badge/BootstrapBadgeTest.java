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
package com.helger.photon.bootstrap5.badge;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for class {@link BootstrapBadge}.
 *
 * @author Philip Helger
 */
public final class BootstrapBadgeTest
{
  @Test
  public void testDefault ()
  {
    assertEquals ("<span class=\"badge\">7</span>", getAsHTMLString (BootstrapBadge.createNumeric (7)));
  }

  @Test
  public void testTypeAndPill ()
  {
    // Bootstrap 5.3 uses "text-bg-*" and no longer "badge-*"
    assertEquals ("<span class=\"badge text-bg-danger rounded-pill\">7</span>",
                  getAsHTMLString (new BootstrapBadge (EBootstrapBadgeType.DANGER).setPill (true).addChild ("7")));
  }

  @Test
  public void testCreateOnDemand ()
  {
    assertEquals (null, BootstrapBadge.createOnDemand (null));
  }
}
