package Trnity.ITP.Recetta.di

import Trnity.ITP.Recetta.Data.remote.api.ApiClient
import Trnity.ITP.Recetta.Data.remote.api.IngredientApiService
import Trnity.ITP.Recetta.Data.remote.api.InventoryApiService
import Trnity.ITP.Recetta.Data.remote.api.RecipeApiService
import Trnity.ITP.Recetta.Data.remote.api.UserApiService
import Trnity.ITP.Recetta.Data.remote.repository.IngredientRepositoryImplementation
import Trnity.ITP.Recetta.Data.remote.repository.InventoryRepositoryImplementation
import Trnity.ITP.Recetta.Data.remote.repository.RecipeRepositoryImplementation
import Trnity.ITP.Recetta.Data.remote.repository.UserRepositoryImplementation
import Trnity.ITP.Recetta.Model.repositories.IngredientRepository
import Trnity.ITP.Recetta.Model.repositories.InventoryRepository
import Trnity.ITP.Recetta.Model.repositories.RecipeRepository
import Trnity.ITP.Recetta.Model.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provides an instance of UserApiService using ApiClient
    @Provides
    @Singleton
    fun provideUserApiService(): UserApiService = ApiClient.create(UserApiService::class.java)

    // Provides an instance of UserRepository using UserRepositoryImplementation
    @Provides
    @Singleton
    fun provideUserRepository(userApiService: UserApiService): UserRepository =
        UserRepositoryImplementation(userApiService)


    // Provides an instance of IngredientApiService using ApiClient
    @Provides
    @Singleton
    fun provideIngredientApiService(): IngredientApiService =
        ApiClient.create(IngredientApiService::class.java)




    // Provides an instance of IngredientRepository using IngredientRepositoryImplementation
    @Provides
    @Singleton
    fun provideIngredientRepository(apiService: IngredientApiService): IngredientRepository =
        IngredientRepositoryImplementation(apiService)




    // Provides an instance of IngredientApiService using ApiClient
    @Provides
    @Singleton
    fun provideRecipeApiService(): RecipeApiService =
        ApiClient.create(RecipeApiService::class.java)


    // Provides an instance of RecipeRepository using IngredientRepositoryImplementation
    @Provides
    @Singleton
    fun provideRecipeRepository(apiService: RecipeApiService): RecipeRepository =
        RecipeRepositoryImplementation(apiService)


    @Provides
    @Singleton
    fun provideInventoryRepository(apiService: InventoryApiService): InventoryRepository =
        InventoryRepositoryImplementation(apiService)

    @Provides
    @Singleton
    fun provideInventoryApiService(): InventoryApiService =
        ApiClient.create(InventoryApiService::class.java)
}