# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repo: api-cp-crime-hearing

Exposes hearing lifecycle data (case timeline, defendant attendance, defendant/offence identity) to
Remand and Sentencing (RaS) and HMPPS/Prison consumers.

**Pattern**: Pure spec-only
**OpenAPI spec version**: 1.0.0 (placeholder — CI overwrites it via `hmcts/update-openapi-version` before
publish, per `docs/OPENAPI-SPEC-VERSIONING.md`)
**OpenAPI Generator version**: 7.23.0
**Spring Boot version**: 4.1.0

## API Endpoint(s)

| Method & path | Responses |
|---|---|
| `GET /hearings/cases/{caseURN}/timeline` | 200, 400, 401, 403, 404, 500 |
| `GET /hearings/{hearingId}/attendance` | 200, 400, 401, 403, 404, 500 |
| `GET /hearings/{hearingId}/cases/{caseURN}/defendants` | 200, 400, 401, 403, 404, 500 |

All operations are read-only and secured by OAuth2 client-credentials (`oAuthJwt`, scope `hearing.read`).

## Generated Interfaces & Schema

- No external JSON Schema files — all schemas are defined inline under `components/schemas` in
  `openapi-spec.yml` (repo convention, see `docs/OPENAPI-FILE-CONVENTIONS.md`).
  `src/main/resources/openapi/schema/deleteme.md` is an unused placeholder kept only so the empty
  `schema/` directory persists in git.
- Generated API interface: `uk.gov.hmcts.cp.openapi.api.HearingsApi`
- Generated models: `HearingTimelineView`, `HearingSummaryView`, `NextAppearance`,
  `DefendantAttendanceView`, `DefendantAttendance`, `AttendanceDay`, `DefendantView`, `OffenceView`,
  `ErrorResponse`

## Domain Models

| Model | Purpose |
|---|---|
| `HearingTimelineView` | Chronological hearing events for a case, plus the next appearance |
| `HearingSummaryView` | One hearing event (date, court, room, type, outcome) |
| `NextAppearance` | Next scheduled appearance following a hearing |
| `DefendantAttendanceView` | Attendance records for all defendants at a hearing |
| `DefendantAttendance` | Per-defendant attendance, across one or more days |
| `AttendanceDay` | How a defendant attended on a given day (`IN_PERSON` / `VIDEO_LINK` / `DID_NOT_APPEAR`) |
| `DefendantView` | Defendant identity and offences scoped to a hearing/case |
| `OffenceView` | A single offence and its current status |
| `ErrorResponse` | Standard error shape (`error`, `message`, `details`, `timestamp`, `traceId`) |

## Test Structure

| Class | What it validates |
|---|---|
| `OpenApiObjectsTest` | Generated model/interface contract — field and method names on generated classes, and that `ErrorResponse.timestamp` generates as `Instant` |

## Generator Config Notes

- Missing `@JsonInclude(NON_NULL)` in `additionalModelTypeAnnotations` (`gradle/openapi.gradle`) — null
  fields will appear in JSON responses, unlike the team standard.

## CI/CD Deviations

- Standard workflow set present (`ci-draft`, `ci-released`, `lint-openapi`, `code-analysis`, `codeql`,
  `secrets-scanner`, `auto-merge-dependabot`).
- Extra workflow beyond the standard set: `publish-api-docs.yml` — publishes this spec's Swagger UI to
  GitHub Pages via the shared `hmcts/amp-catalog` `publish-swagger-ui.yml` reusable workflow on release.

## Repo-Specific Notes

- Response examples are externalised under `src/main/resources/openapi/examples/*.yaml` (one Example
  Object — `summary` + `value` — per file), wired into `openapi-spec.yml` via local `$ref` inside each
  response's `examples:` map. `docs/OPENAPI-FILE-CONVENTIONS.md`'s "no external `$ref`" rule is written
  about `components/schemas`, not examples — it's unverified whether externalised examples still render
  correctly after a SwaggerHub/APIHub publish.
- Runtime API versioning is via the `Accept` header media type
  `application/vnd.hmcts.cp.v<MAJOR>[.<MINOR>[.<PATCH>]]+json`, never the URL path — see
  `docs/API-VERSIONING-STRATEGY.md`.