/**
 * Copyright © 2016-2018 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.dao.service.queue.cassandra;

import org.springframework.stereotype.Component;
import org.thingsboard.server.common.msg.TbMsg;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UnprocessedMsgFilter {

    public Collection<TbMsg> filter(List<TbMsg> msgs, List<MsgAck> acks) {
        Set<UUID> processedIds = acks.stream().map(MsgAck::getMsgId).collect(Collectors.toSet());
        return msgs.stream().filter(i -> !processedIds.contains(i.getId())).collect(Collectors.toList());
    }
}
