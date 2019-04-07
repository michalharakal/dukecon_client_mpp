package org.dukecon.remote.conference.mapper

import org.dukecon.android.api.model.Keycloak
import org.dukecon.data.model.KeycloakEntity

class KeycloakEntityMapper : EntityMapper<Keycloak, KeycloakEntity> {

    override fun mapFromRemote(type: org.dukecon.android.api.model.Keycloak): KeycloakEntity {
        return KeycloakEntity(
                type.realm,
                type.authServerUrl,
                type.sslRequired,
                type.resource,
                type.redirectUri,
                type.useAccountManagement!!.toBoolean()
        )
    }
}