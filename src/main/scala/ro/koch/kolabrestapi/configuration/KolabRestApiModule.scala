package ro.koch.kolabrestapi.configuration

import com.google.inject.{AbstractModule,Provides, Singleton}
import com.google.inject.servlet.{RequestScoped}
import com.google.common.collect.Iterables._
import ro.koch.kolabrestapi.{Routes,PaginationRange,Preconditions}
import ro.koch.kolabrestapi.models.Collection
import Preconditions._
import ro.koch.kolabrestapi.Routes.PathParams
import ro.koch.kolabrestapi.Routes.PathTemplate._
import ro.koch.resourcefacades.FacadeRegistry
import org.apache.abdera2.Abdera
import ro.koch.kolabrestapi.storage.{Storages,ConnectedStorage,CollectionStorage}
import javax.ws.rs.core._
import HttpHeaders._

class KolabRestApiModule extends AbstractModule {
    val ETAG_NONE = new EntityTag("NO-ETAG-PROVIDED");

    def configure() : Unit = {
                bind(classOf[Routes])
                bind(classOf[Storages]).asEagerSingleton()
    }

    @Singleton
    @Provides def abdera:Abdera = new Abdera (null)

    @RequestScoped
    @Provides def connectedStorage(storages:Storages, pathParams:PathParams):ConnectedStorage =
      storages.getForAuthority(pathParams.get(AUTHORITY))

    @RequestScoped
    @Provides def collection(connectedStorage:ConnectedStorage, pathParams:PathParams):Collection =
      connectedStorage.getCollection(pathParams.get(COLLECTION))

    @RequestScoped
    @Provides def collectionStorage(connectedStorage:ConnectedStorage, pathParams:PathParams) =
      connectedStorage.getCollectionStorage(pathParams.get(COLLECTION))

    @RequestScoped
    @Provides def preconditions(headers:HttpHeaders, request:Request):Preconditions =
      request getMethod match {
        case "GET" | "HEAD" => new GetHeadPreconditions(headers)
        case "PUT" | "DELETE" => new PutDeletePreconditions(headers)
        case method => throw new IllegalArgumentException("bad HTTP method: " + method)
      }

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