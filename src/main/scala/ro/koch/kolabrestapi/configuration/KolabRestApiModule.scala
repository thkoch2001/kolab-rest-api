package ro.koch.kolabrestapi.configuration

import com.google.inject.{AbstractModule,Provides, Singleton}
import com.google.inject.servlet.{RequestScoped}
import ro.koch.kolabrestapi.{Routes,PaginationRange}
import ro.koch.kolabrestapi.Routes.PathParams
import ro.koch.kolabrestapi.Routes.PathParams.AUTHORITY
import org.apache.abdera2.Abdera
import ro.koch.kolabrestapi.storage.{Storages,ConnectedStorage,CollectionStorage}
import javax.ws.rs.core._

class KolabRestApiModule extends AbstractModule {
    def configure() : Unit = {
                bind(classOf[Routes])
                bind(classOf[Storages]).asEagerSingleton()
    }

    @Singleton
    @Provides def abdera:Abdera = new Abdera (null)

    @Provides def connectedStorage(storages:Storages, pathParams:PathParams):ConnectedStorage =
      storages.getForAuthority(pathParams.get(AUTHORITY))

    @RequestScoped
    @Provides def paginationRange(uriInfo:UriInfo):PaginationRange = {
      val queryParams = uriInfo getQueryParameters
      val offset = intQueryParam(queryParams, "offset", 0)
      val limit = intQueryParam(queryParams, "limit", 20)
      return new PaginationRange(offset, limit)
    }

    private def intQueryParam(qParams: MultivaluedMap[String,String], name:String, default:Int):Int = {
      try {
        qParams getFirst name toInt
      } catch {
        case _ => default
      }
    }
}