# DigitalOcean App Platform

Add a service from this GitHub repo, branch **`main`**, autodeploy on push. App Platform uses the root Dockerfile when it sees it — leave that as the build strategy. There is no Java buildpack on App Platform.

HTTP port **8080** (Spring listens on `server.port` / `SERVER_PORT`; set the component port to 8080 so they match).

**Do not attach a public HTTP route.** `POST /token` is unauthenticated. Callers in the same app use `${nxt-sts.PRIVATE_URL}` (already `http://…:8080`). Bindable names use the **component `name`** from the app spec (prefix at most 32 characters). Example name below: `nxt-sts`.

This process needs no Valkey, Redis, or other datastore.

## Health check

App Platform **does not** run the Dockerfile `HEALTHCHECK`. Configure the component's own probe or a deploy can look healthy on TCP while the process is not serving.

| Setting | Value |
|---|---|
| Kind | HTTP |
| Path | `GET /actuator/health` |
| Port | **8080** (same as the HTTP port) |
| Expect | `200` and `{"status":"UP"}` |

Control panel: the component → **Health Check** → HTTP, path `/actuator/health`.

## Same app: nxt-device-messaging

If [nxt-device-messaging](https://github.com/nxtgrid/nxt-device-messaging) is another component in this app, set **on that component** (not here):

```text
NXT_STS_URL=${nxt-sts.PRIVATE_URL}
```

Do not use `*.ondigitalocean.app` for this hop. Enable `{ "id": "nxt-sts" }` in device-messaging's `plugins[]` only if that process should mint STS tokens. Details: that repo's [App Platform guide](https://github.com/nxtgrid/nxt-device-messaging/blob/main/docs/deployment/digital-ocean-app-platform.md).

## Same app: another HTTP caller

Any other component that mints tokens should use the same private URL (`${nxt-sts.PRIVATE_URL}`) as its STS base. Keep decoder keys in the caller; this service does not store them.
