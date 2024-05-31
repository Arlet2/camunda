package org.joulin.repos

import org.springframework.data.jpa.repository.JpaRepository
import org.joulin.core.AdRequest
import org.joulin.core.enums.AdRequestStatus

interface AdRequestRepo : JpaRepository<AdRequest, Long> {
    fun findAllByStatus(status: AdRequestStatus): List<AdRequest>
    fun findAllByOwnerId(ownerId: Long): List<AdRequest>
    fun findAllByOwnerIdAndStatus(ownerId: Long, status: AdRequestStatus): List<AdRequest>
}