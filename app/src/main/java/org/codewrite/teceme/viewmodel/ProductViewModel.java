package org.codewrite.teceme.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.paginate.Paginate;

import org.codewrite.teceme.model.rest.ProductJson;
import org.codewrite.teceme.model.rest.StoreProductJson;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.model.room.StoreProductEntity;
import org.codewrite.teceme.repository.ProductRepository;

import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository productRepository;
    private final int loadSize = 50;
    private int page;
    private boolean moreItem;
    private boolean isLoading;
    private MutableLiveData<List<ProductEntity>> productListResult = new MutableLiveData<>();

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
        page = 1;
        moreItem = true;
        isLoading = false;
    }

    public LiveData<List<ProductEntity>> getProducts(final Integer category_id) {
        if (category_id == null) {
            return productRepository.getAllProducts(loadSize, (page * loadSize - loadSize));
        }
        return productRepository.getProductsByCategoryId(category_id);
    }

    public void loadProducts(Integer categoryId) {
        Call<List<ProductJson>> productList = productRepository.getProductList(categoryId, loadSize, page);
        productList.enqueue(new Callback<List<ProductJson>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductJson>> call,
                                   @NonNull final Response<List<ProductJson>> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    ProductEntity[] productEntities = new ProductEntity[response.body().size()];
                    int i = 0;
                    for (ProductJson productJson : response.body()) {
                        productEntities[i++] = productJson;
                    }
                    productRepository.insert(new ProductRepository.CompleteAllCallback() {
                        @Override
                        public void finish() {
                            if (response.headers().get("MoreItem") != null) {
                                moreItem = true;
                                page++;
                            } else {
                                moreItem = false;
                            }
                            isLoading = false;

                            final LiveData<List<ProductEntity>> allProducts =
                                    productRepository.getAllProducts(loadSize * page, 0);
                            allProducts.observeForever(new Observer<List<ProductEntity>>() {
                                @Override
                                public void onChanged(List<ProductEntity> entities) {
                                    if (entities == null) {
                                        return;
                                    }
                                    productListResult.postValue(entities);
                                    allProducts.removeObserver(this);
                                }
                            });
                        }
                    }, productEntities);
                } else {
                    isLoading = false;
                    moreItem = false;

                    final LiveData<List<ProductEntity>> allProducts =
                            productRepository.getAllProducts(loadSize * page, 0);
                    allProducts.observeForever(new Observer<List<ProductEntity>>() {
                        @Override
                        public void onChanged(List<ProductEntity> entities) {
                            if (entities == null) {
                                moreItem = false;
                                return;
                            }
                            if (entities.size() >= loadSize * page) {
                                moreItem = true;
                                page++;
                                productListResult.postValue(entities);
                            } else {
                                moreItem = false;
                            }
                            allProducts.removeObserver(this);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductJson>> call, @NonNull Throwable t) {
                Log.d("ProductViewModel", "onFailure: " + t.getMessage());
                moreItem = false;
                isLoading = false;
                final LiveData<List<ProductEntity>> allProducts =
                        productRepository.getAllProducts(loadSize * page, 0);
                allProducts.observeForever(new Observer<List<ProductEntity>>() {
                    @Override
                    public void onChanged(List<ProductEntity> entities) {
                        if (entities == null) {
                            moreItem = false;
                            return;
                        }
                        if (entities.size() >= loadSize * page) {
                            moreItem = true;
                            page++;
                            productListResult.postValue(entities);
                        } else {
                            moreItem = false;
                        }
                        allProducts.removeObserver(this);
                    }
                });
            }
        });
        isLoading = true;
    }

    public void loadProduct(Integer product_id) {
        Call<ProductJson> productList = productRepository.loadProduct(product_id);
        productList.enqueue(new Callback<ProductJson>() {
            @Override
            public void onResponse(@NonNull Call<ProductJson> call,
                                   @NonNull final Response<ProductJson> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    productRepository.insert(null,response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductJson> call, @NonNull Throwable t) {
                Log.d("ProductViewModel", "onFailure: " + t.getMessage());
            }
        });
    }

    public void loadStoreProduct(Integer store_id) {
        Call<List<StoreProductJson>> storeProductList = productRepository.loadStoreProduct(store_id);
        storeProductList.enqueue(new Callback<List<StoreProductJson>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoreProductJson>> call,
                                   @NonNull final Response<List<StoreProductJson>> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    StoreProductEntity[] entities = new StoreProductEntity[response.body().size()];
                    int i = 0;
                    for (StoreProductJson productJson : response.body()) {
                        entities[i++] = productJson;
                    }
                    productRepository.insert(null,entities);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoreProductJson>> call, @NonNull Throwable t) {
                Log.d("ProductViewModel", "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<ProductEntity> getProduct(Integer product_id) {
        return productRepository.getProduct(product_id);
    }

    public LiveData<List<ProductEntity>> searchProducts(String query) {
        return productRepository.searchProducts(query);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isMoreItem() {
        return moreItem;
    }

    public MutableLiveData<List<ProductEntity>> getProductListResult() {
        return productListResult;
    }

    public void deleteAll() {
    }

    public void invalidate() {
        page = 1;
        moreItem = true;
        isLoading = false;
    }

    public void searchOnlineProducts(String s) {
        Call<List<ProductJson>> productList = productRepository.searchOnlineProducts(s);
        productList.enqueue(new Callback<List<ProductJson>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductJson>> call,
                                   @NonNull final Response<List<ProductJson>> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    ProductEntity[] productEntities = new ProductEntity[response.body().size()];
                    int i = 0;
                    for (ProductJson productJson : response.body()) {
                        productEntities[i++] = productJson;
                    }
                    productRepository.insert(new ProductRepository.CompleteAllCallback() {
                        @Override
                        public void finish() {
                            if (response.headers().get("MoreItem") != null) {
                                moreItem = true;
                                page++;
                            } else {
                                moreItem = false;
                            }
                            isLoading = false;

                            final LiveData<List<ProductEntity>> allProducts =
                                    productRepository.getAllProducts(loadSize * page, 0);
                            allProducts.observeForever(new Observer<List<ProductEntity>>() {
                                @Override
                                public void onChanged(List<ProductEntity> entities) {
                                    if (entities == null) {
                                        return;
                                    }
                                    productListResult.postValue(entities);
                                    allProducts.removeObserver(this);
                                }
                            });
                        }
                    }, productEntities);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductJson>> call, @NonNull Throwable t) {
                Log.d("ProductDataSource", "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<ProductEntity> searchProduct(String query) {
        return productRepository.searchProduct("%" + query + "%");
    }
}
