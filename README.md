# ph-oton-bootstrap5

<!-- ph-badge-start -->
[![Sonatype Central](https://maven-badges.sml.io/sonatype-central/com.helger.photon/ph-oton-bootstrap5-parent-pom/badge.svg)](https://maven-badges.sml.io/sonatype-central/com.helger.photon/ph-oton-bootstrap5-parent-pom/)
[![javadoc](https://javadoc.io/badge2/com.helger.photon/ph-oton-bootstrap5/javadoc.svg)](https://javadoc.io/doc/com.helger.photon/ph-oton-bootstrap5)

> If this project saved you some time or made your day a little easier, a star would mean a lot — it helps others find it too.
<!-- ph-badge-end -->

Java wrapper library for Bootstrap 5 UI components, providing type-safe server-side rendering for web applications.

## Project Overview

This set of Java libraries forms a package to build Java web applications with Bootstrap 5.

### Contained Subprojects

* **ph-oton-bootstrap5** - Java Wrapper for the Bootstrap 5 controls
* **ph-oton-bootstrap5-uictrls** - Special UI controls for Bootstrap 5
* **ph-oton-bootstrap5-pages** - Predefined UI pages with Bootstrap 5 styling
* **ph-oton-bootstrap5-stub** - Servlet stub for Bootstrap 5 web applications
* **ph-oton-bootstrap5-demo** - A standalone demo web application to be run in Tomcat or in provided Jetty

## Requirements

* **Java 17+** is required for building 
* **Application server requirement JakartaEE 10:**
  * At least Tomcat 10.1
  * Jetty 12.x with AnnotationConfiguration enabled

## Maven Usage

Replace `x.y.z` with the effective version number.

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.helger.photon</groupId>
      <artifactId>ph-oton-bootstrap5-parent-pom</artifactId>
      <version>x.y.z</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

### Bootstrap 5

To use Bootstrap 5 front end:

```xml
<dependencies>
  <dependency>
    <groupId>com.helger.photon</groupId>
    <artifactId>ph-oton-bootstrap5-stub</artifactId>
  </dependency>
</dependencies>
```

See the submodule `ph-oton-bootstrap5-demo` for a working example project with Bootstrap 5 UI.

## Key Features

* Type-safe Java wrapper for Bootstrap 5 components
* Server-side component rendering
* Support for all major Bootstrap 5 components:
  * Grid system with XXL breakpoint
  * Forms with floating labels
  * Cards, modals, navbars, and dropdowns
  * New Offcanvas component
  * Alerts, badges, buttons, and more
* Integration with ph-oton framework
* JakartaEE 10 compatible

## Bootstrap 5 Highlights

This library wraps **Bootstrap 5.3.x**, which includes:

* **No jQuery dependency** - Bootstrap 5 uses vanilla JavaScript
* **New components** - Offcanvas sidebar navigation
* **Enhanced forms** - Floating labels and improved validation
* **Updated grid** - New XXL breakpoint for larger screens (≥1400px)
* **Modernized styling** - Cleaner CSS and improved utilities
* **Updated data attributes** - All Bootstrap data attributes now use `data-bs-*` prefix

## Migration from Bootstrap 4

See [migration-plan.md](migration-plan.md) for detailed migration steps from ph-oton-bootstrap4.

## License

Licensed under the Apache License, Version 2.0.

## News and Noteworthy

v0.9.5 - work in progress
* Requires at least ph-schedule 6.2.0. The `ph-schedule-parent-pom` BOM is imported explicitly in the parent POM, ahead of `ph-oton-parent-pom`, so that it wins over the version pinned by ph-oton
* `BasePageAppInfoScheduler` now shows the minimum, average and maximum runtime per job, from the timer statistics that `StatisticsJobListener` collects since ph-schedule 6.2.0
* `BasePageAppInfoScheduler` now shows the most recent failures per job - date, exception class and message, plus the stack trace of the most recent one - from the `JobExecutionErrorRegistry` introduced in ph-schedule 6.2.0. Previously only the number of errors was visible and the reason had to be looked up in the log file
* `BasePageAppInfoScheduler` now uses `StatisticsJobListener.getStatisticsName (Class)` and the `STATS_SUFFIX_*` constants instead of rebuilding the statistics handler names itself
* The demo application now additionally schedules the new job `DemoDelayedJob` that runs exactly once, 10 minutes after application startup. In contrast to `DemoLongRunningJob` it is a plain scope aware job and not a long running one, so it only shows up on the "Scheduler" page. Until it fires, its trigger start time lies in the future and is therefore marked with a red badge
* **Fixed**: `BasePageAppInfoScheduler` reported "Standby" for a scheduler that was never started, and its "Not started" state was unreachable. `IScheduler.isStarted ()` is implemented as "was ever started" by `StdScheduler` and therefore returns `true` in standby mode as well - the start date is now used to tell the two apart, matching `ESchedulerState.NOT_STARTED` of ph-schedule 6.2.0
* `BasePageAppInfoLongRunningJobs` now shows the duration a currently running job is already running - in the "Duration" column of the list and as the new "Duration so far" entry of the detail view.
  Added the new text constant `BasePageAppInfoLongRunningJobs.EText.MSG_DURATION_SO_FAR`
* `BasePageAppInfoScheduler` now shows the details of all currently executing jobs (job key, job class, trigger key, fire time, duration so far, scheduled fire time, next fire time, refire count, recovering state and fire instance ID) instead of only their count.
  Added the new text constants `BasePageAppInfoScheduler.EText.MSG_FIRE_TIME`, `MSG_SCHEDULED_FIRE_TIME`, `MSG_RUNNING_FOR`, `MSG_REFIRE_COUNT`, `MSG_RECOVERING` and `MSG_FIRE_INSTANCE_ID`
* `BasePageAppInfoScheduler` now shows a "Refresh" button on top of the page, as the other pages do
* The demo application now schedules the new dummy job `DemoLongRunningJob` that starts immediately, logs a message, sleeps for 10 seconds and repeats every 30 seconds, so that both "Long running jobs" and "Scheduler" show a currently running job
* `BasePageAppInfoScheduler` now shows a badge next to every date time, stating the distance to "now" (e.g. "in 27 seconds" or "2 seconds ago"). The badge is green if the date time is on the expected side of "now" - start time, previous fire time, fire time and scheduled fire time are expected to be in the past, end time and next fire time in the future - and red otherwise.
  Added the new text constants `BasePageAppInfoScheduler.EText.MSG_TIME_IN` and `MSG_TIME_AGO`; `BasePageAppInfoScheduler.EText` now implements `IHasDisplayTextWithArgs` instead of `IHasDisplayText`
* `BasePageAppInfoScheduler` now shows "none" instead of "null" for date times that are not set (e.g. the previous fire time of a trigger that never fired)
* `BasePageAppInfoScheduler` now shows a set of diagnostic details that help to answer the question why a job did not run:
  * The state of every trigger (`IScheduler.getTriggerState`) as a badge - only `NORMAL` is green, `ERROR` is red, `PAUSED` and `BLOCKED` are yellow
  * The scheduler state as a badge plus a prominent error/warning box if the scheduler is shut down, in standby or was never started, because then no job is executed at all
  * `ITrigger.mayFireAgain ()` and the final fire time, to tell an exhausted trigger apart from a paused or broken one
  * The paused trigger groups (`IScheduler.getPausedTriggerGroups`)
  * The thread pool utilization, with a red badge and a warning box if all threads are in use and further jobs have to wait
  * The next `BasePageAppInfoScheduler.NEXT_FIRE_TIMES_COUNT` (5) fire times per trigger, to verify that e.g. a cron expression means what it is supposed to mean
  * The registered calendars of the scheduler and the calendar of every trigger, because exclusion calendars silently suppress fire times
  * All jobs that have no trigger at all and are therefore never executed
  * The execution, error and vetoed counters that the `StatisticsJobListener` of `GlobalQuartzScheduler` collects per job, with a red badge if the error count is &gt; 0
  Added the new public constant `BasePageAppInfoScheduler.NEXT_FIRE_TIMES_COUNT` and the new text constants `BasePageAppInfoScheduler.EText.MSG_SCHEDULER_STATE`, `MSG_SCHEDULER_STATE_RUNNING`, `MSG_SCHEDULER_STATE_STANDBY`, `MSG_SCHEDULER_STATE_SHUTDOWN`, `MSG_SCHEDULER_STATE_NOT_STARTED`, `MSG_SCHEDULER_STANDBY_HINT`, `MSG_SCHEDULER_SHUTDOWN_HINT`, `MSG_SCHEDULER_NOT_STARTED_HINT`, `MSG_RUNNING_SINCE`, `MSG_THREAD_POOL`, `MSG_THREAD_POOL_USAGE`, `MSG_THREAD_POOL_EXHAUSTED`, `MSG_PAUSED_TRIGGER_GROUPS`, `MSG_PAUSED_TRIGGER_GROUPS_HINT`, `MSG_CALENDARS`, `MSG_JOBS_WITHOUT_TRIGGER`, `MSG_JOBS_WITHOUT_TRIGGER_HINT`, `MSG_TRIGGER_STATE`, `MSG_CALENDAR_NAME`, `MSG_MAY_FIRE_AGAIN`, `MSG_FINAL_FIRE_TIME`, `MSG_NEXT_FIRE_TIMES`, `MSG_EXECUTION_COUNT`, `MSG_ERROR_COUNT` and `MSG_VETOED_COUNT`

v0.9.4 - 2026-09-04
* Requires at least ph-oton 10.5.0

v0.9.3 - 2026-08-30
* Requires at least ph-oton 10.4.0
* Added the new page `BasePageAppInfoLongRunningJobs` that shows the currently running long running jobs as well as the results of all previously finished ones, and that allows to delete single results or all of them. The list and the detail view also show the job type introduced in ph-oton 10.4.0.
  It is registered as `BootstrapPagesMenuConfigurator.MENU_ADMIN_APPINFO_LONG_RUNNING_JOBS` below "Administration / Application Information".
  Added the new overload `BootstrapPagesMenuConfigurator.addAppInfoItems (IMenuTree, IMenuItem, IMenuObjectFilter, GoMappingManager, WebSiteResourceBundleManager, LongRunningJobManager, ILongRunningJobResultManager)`.
  The 3 argument version of `addAppInfoItems` adds the new page using the managers of `PhotonBasicManager`. The existing 5 argument version does not add the new page, so that its behaviour is unchanged.

v0.9.2 - 2026-08-20
* Added new class `BootstrapFormSettings` that centrally manages the default grid specifications of `BootstrapForm` and `BootstrapViewForm`.
  It offers `getDefaultLeftGrid ()`, `getDefaultRightGrid ()`, `setDefaultLeftGrid (BootstrapGridSpec)` and `setDefaultSplitting (BootstrapGridSpec, BootstrapGridSpec)`
* Removed `BootstrapForm.DEFAULT_LEFT_PART`, `BootstrapForm.DEFAULT_RIGHT_PART`, `BootstrapViewForm.DEFAULT_LEFT_PART` and `BootstrapViewForm.DEFAULT_RIGHT_PART` in favour of `BootstrapFormSettings.DEFAULT_LEFT_PART`, `BootstrapFormSettings.DEFAULT_LEFT_GRID` and `BootstrapFormSettings.DEFAULT_RIGHT_GRID`
* `BootstrapViewForm` now uses the same default grid as `BootstrapForm` (`col-12 col-sm-2` for the left part instead of `col-3`)

v0.9.1 - 2026-08-19
* Added `BootstrapGridSpec.Builder` incl. the static factory methods `BootstrapGridSpec.builder ()` and `BootstrapGridSpec.builder (BootstrapGridSpec)` for easier creation of grid specifications
* Added `BootstrapGridSpec.getInverse ()` to create the complementary grid specification, so that two grid specifications add up to the maximum number of parts per breakpoint
* `BootstrapForm.setLeft (...)` and `BootstrapViewForm.setLeft (...)` now derive the right grid via `BootstrapGridSpec.getInverse ()`.
  For breakpoints that are not set at all (as in `setLeft (-1, -1, 3, 3, 2, 2)`) the right side now gets `col-12` instead of no grid class at all
* The `BootstrapDateTimePicker` calendar headline now shows the four digit year (e.g. "August 2026") instead of the Tempus Dominus default two digit year (e.g. "August 26"), as in the Bootstrap 4 version.
  Added `BootstrapDateTimePicker.dayViewHeaderFormat ()`, `setDayViewHeaderFormat (JSAssocArray)` and `createDefaultDayViewHeaderFormat ()` to customize the new `localization.dayViewHeaderFormat` option

v0.9.0 - 2026-08-19
* Initial release for Bootstrap 5.3.x
* Migration from Bootstrap 4 wrapper
* Full support for Bootstrap 5 components
* Updated for JakartaEE 10
* Reworked `BootstrapInputGroup` to use the flat Bootstrap 5 markup (no more `input-group-prepend`/`input-group-append` wrappers)
* Made the DateTimePicker based on Tempus Dominus v6.10.4 work: fixed initialization (`new tempusDominus.TempusDominus`), format token mapping, icon configuration and initial value handling; clicking into the input opens the picker as well (as in the Bootstrap 4 version)
* Added Popper v2.11.8 as a contained resource, as it is required by Tempus Dominus for popup positioning
* Migrated `BootstrapTooltip` and `BootstrapModal` from the removed jQuery plugin API to the native `bootstrap.Tooltip`/`bootstrap.Modal` JS API
* Horizontal form labels now use the `col-form-label` class for correct alignment
* Added new components: `BootstrapOffcanvas`, `BootstrapFormFloating` (floating labels) and `BootstrapValidFeedback`
* Added the missing `EXPAND_XXL` entry to `EBootstrapNavbarExpandType`
* Updated Tempus Dominus from v6.9.4 to v6.10.4 (keyboard navigation and ARIA improvements). The shipped files are unmodified upstream copies - the ph-oton specific integration is documented in `ph-oton-bootstrap5-uictrls/src/main/resources/external/tempusdominus/README.md`
* Added `BootstrapDateTimePicker.setKeyboardNavigation (...)` for the new Tempus Dominus `display.keyboardNavigation` option
* Added `EBootstrap5DateTimePickerTexts.TOGGLE_ARIA_LABEL` and fill the new Tempus Dominus `localization.toggleAriaLabel` key with it
* Added the demo page "Misc Controls" that shows all controls without a dedicated demo page (modal, offcanvas, tooltip, collapse, floating labels, validation feedback, cards, collapsible cards, alerts, badges, breadcrumb, list group, dropdown, tabs, tree view, file upload, Select2 and Prism)
* `ph-oton-bootstrap5-uictrls` now uses FontAwesome 6 (`ph-oton-icon-fontawesome6`) instead of FontAwesome 5.
  This affects `BootstrapDateTimePicker` (which now matches the Tempus Dominus default icon set), `BootstrapCardCollapsible` and `BootstrapSimpleTooltip` (`QUESTION_CIRCLE` became `CIRCLE_QUESTION`).
  FontAwesome 6 requires the style class in addition to the icon class, so `bootstrap-ext.css` now matches `.fa-solid` as well

---

My personal [Coding Styleguide](https://github.com/phax/meta/blob/master/CodingStyleguide.md) |
It is appreciated if you star the GitHub project if you like it.
