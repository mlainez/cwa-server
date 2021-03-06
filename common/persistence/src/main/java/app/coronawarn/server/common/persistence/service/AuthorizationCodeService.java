/*
 * Coronalert / cwa-server
 *
 * (c) 2020 Devside SRL
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package app.coronawarn.server.common.persistence.service;

import app.coronawarn.server.common.persistence.domain.DiagnosisKey;
import app.coronawarn.server.common.persistence.domain.authorizationcode.AuthorizationCode;
import app.coronawarn.server.common.persistence.repository.AuthorizationCodeRepository;
import io.micrometer.core.annotation.Timed;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthorizationCodeService {

  private static final Logger logger = LoggerFactory.getLogger(AuthorizationCodeService.class);
  private final AuthorizationCodeRepository authorizationCodeRepository;

  public AuthorizationCodeService(AuthorizationCodeRepository authorizationCodeRepository) {
    this.authorizationCodeRepository = authorizationCodeRepository;
  }

  /**
   * Persists the specified collection of {@link DiagnosisKey} instances. If the key data of a particular diagnosis key
   * already exists in the database, this diagnosis key is not persisted.
   *
   * @param authorizationCodeEntities must not contain {@literal null}.
   * @throws IllegalArgumentException in case the given collection contains {@literal null}.
   */
  @Timed
  @Transactional
  public void saveAuthorizationCodes(Collection<AuthorizationCode> authorizationCodeEntities) {
    for (AuthorizationCode authorizationCode : authorizationCodeEntities) {
      authorizationCodeRepository.saveDoNothingOnConflict(
          authorizationCode.getSignature(), authorizationCode.getMobileTestId(),
          authorizationCode.getDatePatientInfectious(), authorizationCode.getDateTestCommunicated());
    }
  }

}
