/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.metadata;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.metadata.store.InMemoryWritableMetadataService;
import org.apache.dubbo.rpc.model.ApplicationModel;

import static org.apache.dubbo.common.extension.ExtensionLoader.getExtensionLoader;

/**
 * Local {@link MetadataService} that extends {@link MetadataService} and provides the modification, which is used for
 * Dubbo's consumers and providers.
 *
 * @since 2.7.4
 */
@SPI("default")
public interface WritableMetadataService extends MetadataService {

    /**
     * The default storage type value as the extension name
     */
    public static String DEFAULT_METADATA_STORAGE_TYPE = "default";

    /**
     * The remote storage type value as the extension name
     */
    public static String REMOTE_METADATA_STORAGE_TYPE = "remote";

    /**
     * Gets the current Dubbo Service name
     *
     * @return non-null
     */
    @Override
    default String serviceName() {
        return ApplicationModel.getApplication();
    }

    /**
     * Exports a {@link URL}
     *
     * @param url a {@link URL}
     * @return If success , return <code>true</code>
     */
    boolean exportURL(URL url);

    /**
     * Unexports a {@link URL}
     *
     * @param url a {@link URL}
     * @return If success , return <code>true</code>
     */
    boolean unexportURL(URL url);

    /**
     * fresh Exports
     *
     * @return If success , return <code>true</code>
     */
    default boolean refreshMetadata(String exportedRevision, String subscribedRevision) {
        return true;
    }

    /**
     * Subscribes a {@link URL}
     *
     * @param url a {@link URL}
     * @return If success , return <code>true</code>
     */
    boolean subscribeURL(URL url);

    /**
     * Unsubscribes a {@link URL}
     *
     * @param url a {@link URL}
     * @return If success , return <code>true</code>
     */
    boolean unsubscribeURL(URL url);

    void publishServiceDefinition(URL providerUrl);

    /**
     * Get {@link ExtensionLoader#getDefaultExtension() the defautl extension} of {@link WritableMetadataService}
     *
     * @return non-null
     * @see InMemoryWritableMetadataService
     */
    static WritableMetadataService getDefaultExtension() {
        return getExtensionLoader(WritableMetadataService.class).getDefaultExtension();
    }

    /**
     * Get the metadata's storage type
     *
     * @param isDefaultStorageType is default storage type or not
     * @return non-null, {@link #DEFAULT_METADATA_STORAGE_TYPE "default"} or {@link #REMOTE_METADATA_STORAGE_TYPE "remote"}
     */
    public static String getMetadataStorageType(boolean isDefaultStorageType) {
        return isDefaultStorageType ? DEFAULT_METADATA_STORAGE_TYPE : REMOTE_METADATA_STORAGE_TYPE;
    }

    static WritableMetadataService getExtension(boolean isDefaultStorageType) {
        return getExtension(getMetadataStorageType(isDefaultStorageType));
    }

    static WritableMetadataService getExtension(String name) {
        return getExtensionLoader(WritableMetadataService.class).getOrDefaultExtension(name);
    }
}