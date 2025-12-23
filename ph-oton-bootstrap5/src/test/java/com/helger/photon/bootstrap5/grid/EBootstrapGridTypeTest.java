/*
 * Copyright (C) 2018-2025 Philip Helger (www.helger.com)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test class for class {@link EBootstrapBreakpoint}.
 *
 * @author Philip Helger
 */
public final class EBootstrapGridTypeTest
{
  @Test
  public void testWidth ()
  {
    assertNull (EBootstrapBreakpoint.getForWidth (-1));
    assertEquals (EBootstrapBreakpoint.XS, EBootstrapBreakpoint.getForWidth (0));
    assertEquals (EBootstrapBreakpoint.XS, EBootstrapBreakpoint.getForWidth (1));
    assertEquals (EBootstrapBreakpoint.XS, EBootstrapBreakpoint.getForWidth (575));
    assertEquals (EBootstrapBreakpoint.SM, EBootstrapBreakpoint.getForWidth (576));
    assertEquals (EBootstrapBreakpoint.SM, EBootstrapBreakpoint.getForWidth (767));
    assertEquals (EBootstrapBreakpoint.MD, EBootstrapBreakpoint.getForWidth (768));
    assertEquals (EBootstrapBreakpoint.MD, EBootstrapBreakpoint.getForWidth (991));
    assertEquals (EBootstrapBreakpoint.LG, EBootstrapBreakpoint.getForWidth (992));
    assertEquals (EBootstrapBreakpoint.LG, EBootstrapBreakpoint.getForWidth (1199));
    assertEquals (EBootstrapBreakpoint.XL, EBootstrapBreakpoint.getForWidth (1200));
    assertEquals (EBootstrapBreakpoint.XXL, EBootstrapBreakpoint.getForWidth (1400));
    assertEquals (EBootstrapBreakpoint.XXL, EBootstrapBreakpoint.getForWidth (Integer.MAX_VALUE));
  }
}
