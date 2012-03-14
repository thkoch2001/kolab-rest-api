package ro.koch.kolabrestapi.configuration

import com.google.inject.{AbstractModule,Provides, Singleton}
import ro.koch.kolabrestapi.Routes
import ro.koch.kolabrestapi.Routes.PathParams
import ro.koch.kolabrestapi.Routes.PathParams.AUTHORITY
import org.apache.abdera2.Abdera
import ro.koch.kolabrestapi.storage.{Storages,ConnectedStorage}

class KolabRestApiModule extends AbstractModule {
    def configure() : Unit = {
                bind(classOf[Routes])
                bind(classOf[Storages]).asEagerSingleton()
    }

    @Singleton
    @Provides def abdera:Abdera = new Abdera (null)

    @Provides def connectedStorage(storages:Storages, pathParams:PathParams):ConnectedStorage =
      storages.getForAuthority(pathParams.get(AUTHORITY))
}