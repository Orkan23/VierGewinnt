package de.htwg.se.VierGewinnt.persist.fileio

import com.google.inject.AbstractModule
import de.htwg.se.VierGewinnt.persist.database.slick.DAOSlickPlayer
import de.htwg.se.VierGewinnt.persist.database.DAOInterface
import de.htwg.se.VierGewinnt.persist.fileio.fileIoJsonImpl.FileIO as FileIoJson
import de.htwg.se.VierGewinnt.persist.fileio.fileIoXmlnImpl.FileIO as FileIoXml

class PersistenceModule extends AbstractModule {
  override def configure(): Unit =
    bind(classOf[FileIOInterface]).to(classOf[FileIoJson])
    // bind(classOf[FileIOInterface]).to(classOf[FileIoXml])

    bind(classOf[DAOInterface]).to(classOf[DAOSlickPlayer])
}
