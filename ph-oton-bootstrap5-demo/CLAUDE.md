# ph-oton-bootstrap5-demo

Standalone demo WAR showing the Bootstrap 5 components in a real ph-oton application. Not deployed to Maven Central. See the root `CLAUDE.md` for the repo-wide rules.

## Running it

`main ()` in `RunInJettyPHOTONDEMO_BS5` (test scope) starts Jetty on http://localhost:8080/ ; `JettyStopPHOTONDEMO_BS5` shuts it down again. This is the fastest way to see a component change in a browser — the component modules have no UI of their own.

## Layout

* `app/` — application-wide config (`CApp`, `AppSettings`, `AppCommonUI`, `AppLayoutHTMLProvider`) and the base page classes used by the demo pages.
* `pub/` and `secure/` — the public and the logged-in area; each has a `menu/` package with a `CMenu*` (item ID constants) and a `Menu*` (tree construction) class, and a `page/` package with the pages themselves.
* `servlet/` — the servlets, the `AppWebAppListener` and the CSP handling.
* `src/main/webapp/` — `WEB-INF/web.xml` plus the application's own `css/default.css` and `js/default.js`; the jscompress/csscompress plugins are configured against this directory here (not against `src/main/resources`), so the committed `.min` variants live next to them.

## Adding a demo page

Create the page under `pub/page/` or `secure/page/`, then register it in the matching `MenuPublic` / `MenuSecure` class with a new item ID constant in `CMenuPublic` / `CMenuSecure`. A page that is not in the menu tree is unreachable.
