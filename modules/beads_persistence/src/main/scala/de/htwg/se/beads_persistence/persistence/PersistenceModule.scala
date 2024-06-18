package de.htwg.se.beads_persistence.persistence

import de.htwg.se.beads_persistence.persistence.persistenceComponent.BeadsPersistenceAPI
import de.htwg.se.beads_persistence.database.slick.BeadsSlickDB
import de.htwg.se.beads_persistence.database.mongodb.BeadsMongoDB
import de.htwg.se.beads_persistence.database.DAO_Interface
object SlickModule {
  given DAO_Interface = BeadsSlickDB
}

object MongoDBModule {
  given DAO_Interface = BeadsMongoDB
}

object BeadsPersistenceAPIModule extends App:
  BeadsPersistenceAPI(using MongoDBModule.given_DAO_Interface).start()
