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
package com.helger.photon.bootstrap5.form;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.helger.html.hc.html.grouping.HCDiv;
import com.helger.photon.bootstrap5.grid.BootstrapGridSpec;

/**
 * Test class for class {@link BootstrapViewForm}.
 *
 * @author Philip Helger
 */
public final class BootstrapViewFormTest
{
  private static String _getClasses (final BootstrapGridSpec aSpec)
  {
    return aSpec.applyTo (new HCDiv ()).getAllClassesAsString ();
  }

  @Test
  public void testSetLeftAllBreakpoints ()
  {
    // The right side is the complement of the left side
    final BootstrapViewForm aForm = new BootstrapViewForm ().setLeft (3);
    assertEquals ("col-3", _getClasses (aForm.getLeft ()));
    assertEquals ("col-9", _getClasses (aForm.getRight ()));
  }

  @Test
  public void testSetLeftSomeBreakpoints ()
  {
    // XS and SM are not set, so the left side has 0 parts and the right side takes the maximum
    final BootstrapViewForm aForm = new BootstrapViewForm ().setLeft (BootstrapGridSpec.builder ()
                                                                                       .md (3)
                                                                                       .xl (2)
                                                                                       .build ());
    assertEquals ("col-md-3 col-xl-2", _getClasses (aForm.getLeft ()));
    assertEquals ("col-12 col-md-9 col-xl-10", _getClasses (aForm.getRight ()));
  }

  @Test
  public void testSetLeftNothing ()
  {
    // 0 parts on the left means the maximum on the right
    final BootstrapViewForm aForm = new BootstrapViewForm ().setLeft (0);
    assertNull (_getClasses (aForm.getLeft ()));
    assertEquals ("col-12", _getClasses (aForm.getRight ()));
  }
}
