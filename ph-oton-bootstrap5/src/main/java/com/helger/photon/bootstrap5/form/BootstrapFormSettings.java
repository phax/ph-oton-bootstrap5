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

import org.jspecify.annotations.NonNull;

import com.helger.annotation.concurrent.GuardedBy;
import com.helger.annotation.concurrent.ThreadSafe;
import com.helger.base.concurrent.SimpleReadWriteLock;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.photon.bootstrap5.grid.BootstrapGridSpec;

/**
 * Central settings for all form like objects - {@link BootstrapForm} as well as
 * {@link BootstrapViewForm}. The values set in here are only used for newly created form objects -
 * existing form objects are not modified.
 *
 * @author Philip Helger
 * @since 0.9.2
 */
@ThreadSafe
public final class BootstrapFormSettings
{
  /** The default grid specification of the left part of a form */
  public static final BootstrapGridSpec DEFAULT_LEFT_GRID = BootstrapGridSpec.builder ().md (4).lg (3).xxl (2).build ();

  /** The default grid specification of the right part of a form */
  public static final BootstrapGridSpec DEFAULT_RIGHT_GRID = DEFAULT_LEFT_GRID.getInverse ();

  private static final SimpleReadWriteLock RW_LOCK = new SimpleReadWriteLock ();
  @GuardedBy ("RW_LOCK")
  private static BootstrapGridSpec s_aLeftGrid = DEFAULT_LEFT_GRID;
  @GuardedBy ("RW_LOCK")
  private static BootstrapGridSpec s_aRightGrid = DEFAULT_RIGHT_GRID;

  private BootstrapFormSettings ()
  {}

  /**
   * @return The default grid specification of the left part of a form. Never <code>null</code>.
   */
  @NonNull
  public static BootstrapGridSpec getDefaultLeftGrid ()
  {
    return RW_LOCK.readLockedGet (() -> s_aLeftGrid);
  }

  /**
   * @return The default grid specification of the right part of a form. Never <code>null</code>.
   */
  @NonNull
  public static BootstrapGridSpec getDefaultRightGrid ()
  {
    return RW_LOCK.readLockedGet (() -> s_aRightGrid);
  }

  /**
   * Set the default grid specification of the left part of a form. The right part is the complement
   * of the left part.
   *
   * @param aLeft
   *        The left grid specification to use. May not be <code>null</code>.
   * @see #setDefaultSplitting(BootstrapGridSpec, BootstrapGridSpec)
   */
  public static void setDefaultLeftGrid (@NonNull final BootstrapGridSpec aLeft)
  {
    ValueEnforcer.notNull (aLeft, "Left");

    // The right side is the complement of the left side
    setDefaultSplitting (aLeft, aLeft.getInverse ());
  }

  /**
   * Set the default grid specifications of the left and the right part of a form.
   *
   * @param aLeft
   *        The left grid specification to use. May not be <code>null</code>.
   * @param aRight
   *        The right grid specification to use. May not be <code>null</code>.
   */
  public static void setDefaultSplitting (@NonNull final BootstrapGridSpec aLeft,
                                          @NonNull final BootstrapGridSpec aRight)
  {
    ValueEnforcer.notNull (aLeft, "Left");
    ValueEnforcer.notNull (aRight, "Right");

    RW_LOCK.writeLocked (() -> {
      s_aLeftGrid = aLeft;
      s_aRightGrid = aRight;
    });
  }
}
