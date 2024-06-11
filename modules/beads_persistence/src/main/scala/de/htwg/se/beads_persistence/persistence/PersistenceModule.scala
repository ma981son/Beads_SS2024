package de.htwg.se.beads_persistence.persistence

import de.htwg.se.beads_persistence.persistence.persistenceComponent.BeadsPersistenceAPI
import de.htwg.se.beads_persistence.database.slick.BeadsSlickDB

object BeadsPersistenceAPIModule extends App:
  BeadsPersistenceAPI().start()
