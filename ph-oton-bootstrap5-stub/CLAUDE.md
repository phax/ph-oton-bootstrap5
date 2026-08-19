# ph-oton-bootstrap5-stub

Servlet bootstrap for Bootstrap 5 applications: an application that depends on this module gets the whole ph-oton stack wired up automatically. See the root `CLAUDE.md` for the repo-wide rules.

## What gets wired, and where

* `PhotonStubServletContainerInitializer` is registered through `META-INF/services/jakarta.servlet.ServletContainerInitializer` — the entry point when the WAR starts. Changing its name means changing that service file too.
* `PhotonStubInitializer.registerDefaultResources ()` decides which CSS/JS every page of every application gets. It registers the Bootstrap CSS/JS from `BootstrapCustomConfig`, the ph-oton uicore resources, FamFam icons and the default meta elements.
* **jQuery 3 is registered globally here** (`EUICoreJSPathProvider.JQUERY_3`) because ph-oton's own uicore JS and the default `IHCOnDocumentReadyProvider` need it. This does not contradict the "no jQuery" rule for Bootstrap components — see the root `CLAUDE.md`.
* Applications override the defaults by calling `BootstrapCustomConfig.setBootstrapCSS (...)` / `setBootstrapJS (...)` *before* initialization, or by shipping their own `PhotonCSS`/`PhotonJS`/`PhotonMetaElements` XML files on the classpath, which are read last.

Because this module has almost no logic of its own, the meaningful test is `SPITest` — it validates that every SPI implementation named in `META-INF/services` actually exists and is instantiable.
