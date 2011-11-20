/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.tmrk.enterprisecloud.features;

import com.google.common.collect.Iterables;
import org.jclouds.tmrk.enterprisecloud.domain.AssignedIpAddresses;
import org.jclouds.tmrk.enterprisecloud.domain.DeviceNetwork;
import org.jclouds.tmrk.enterprisecloud.domain.VirtualMachine;
import org.jclouds.tmrk.enterprisecloud.domain.VirtualMachines;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Tests behavior of {@code VirtualMachineClient}
 * 
 * @author Jason King
 */
@Test(groups = "live", testName = "VirtualMachineClientLiveTest")
public class VirtualMachineClientLiveTest extends BaseTerremarkEnterpriseCloudClientLiveTest {
   @BeforeGroups(groups = { "live" })
   public void setupClient() {
      super.setupClient();
      client = context.getApi().getVirtualMachineClient();
   }

   private VirtualMachineClient client;

   @Test
   public void testGetVirtualMachines() throws Exception {
      // TODO: don't hard-code uri
       VirtualMachines virtualMachines = client.getVirtualMachines(new URI("/cloudapi/ecloud/virtualMachines/computePools/89"));
       for( VirtualMachine vm : virtualMachines.getVirtualMachines()) {
           VirtualMachine virtualMachine = client.getVirtualMachine(vm.getHref());
           assert null != virtualMachine;
           assertEquals(virtualMachine.getStatus(),VirtualMachine.VirtualMachineStatus.DEPLOYED);
       }
   }

   @Test
   public void testGetVirtualMachine() throws Exception {
      // TODO: don't hard-code uri
       VirtualMachine virtualMachine = client.getVirtualMachine(new URI("/cloudapi/ecloud/virtualMachines/5504"));
       assert null != virtualMachine;
       assertEquals(virtualMachine.getStatus(),VirtualMachine.VirtualMachineStatus.DEPLOYED);
   }

    @Test
   public void testGetAssignedIpAddresses() throws Exception {
        AssignedIpAddresses assignedIpAddresses = client.getAssignedIpAddresses(new URI("/cloudapi/ecloud/virtualMachines/5504/assignedips"));
        assert null != assignedIpAddresses;
        DeviceNetwork network = Iterables.getOnlyElement(assignedIpAddresses.getNetworks().getDeviceNetworks());
        Set<String> ipAddresses = network.getIpAddresses().getIpAddresses();
        assertTrue(ipAddresses.size()>0, "vm has no assigned ip addresses");
    }
}
