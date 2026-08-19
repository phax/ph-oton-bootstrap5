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
package com.helger.photon.bootstrap5.grid;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for class {@link BootstrapRow}.
 *
 * @author Philip Helger
 */
public final class BootstrapRowTest
{
  @Test
  public void testSingleColumn ()
  {
    final BootstrapRow aRow = new BootstrapRow ();
    aRow.createColumn (BootstrapGridSpec.builder ().all (6).build ()).addChild ("a");
    assertEquals ("<div class=\"row\"><div class=\"col-6\">a</div></div>", getAsHTMLString (aRow));
  }

  @Test
  public void testAllBreakpoints ()
  {
    final BootstrapRow aRow = new BootstrapRow ();
    aRow.createColumn (BootstrapGridSpec.builder ().xs (12).sm (6).md (4).lg (3).xl (2).xxl (1).build ())
        .addChild ("b");
    // XXL is the Bootstrap 5 specific breakpoint
    assertEquals ("<div class=\"row\"><div class=\"col-12 col-sm-6 col-md-4 col-lg-3 col-xl-2 col-xxl-1\">b</div></div>",
                  getAsHTMLString (aRow));
  }

  @Test
  public void testColumnOrder ()
  {
    final BootstrapRow aRow = new BootstrapRow ();
    aRow.createColumn (BootstrapGridSpec.builder ().all (6).build ())
        .setOrder (EBootstrapColOrder.ORDER_1)
        .addChild ("a");
    assertEquals ("<div class=\"row\"><div class=\"col-6 order-1\">a</div></div>", getAsHTMLString (aRow));
  }
}
