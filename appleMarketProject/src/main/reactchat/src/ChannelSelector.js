import React, { useEffect } from 'react';
import { useSendbirdStateContext } from '@sendbird/uikit-react';

const ChannelSelector = ({ buyerId, sellerId, onChannelSelected }) => {
  const globalStore = useSendbirdStateContext();
  const sdk = globalStore?.stores?.sdkStore?.sdk;

  useEffect(() => {
    const createOrFetchChannel = async () => {
      try {
        // 채널 생성 또는 기존 채널 재사용
        const params = {
          invitedUserIds: [buyerId, sellerId],  // 초대할 사용자 ID 배열
          name: `${sellerId}`,  // 채널 이름
          isDistinct: true,  // 동일한 사용자들로 된 기존 채널이 있으면 재사용
          customType: 'buyer-seller-chat',  // 커스텀 타입 (선택)
        };

        const channel = await sdk.groupChannel.createChannel(params);  // 채널 생성 또는 기존 채널 반환
        console.log('New or existing channel:', channel.url);
        onChannelSelected(channel.url);  // 채널 선택 시 처리 로직
      } catch (error) {
        console.error('Error creating or fetching channel:', error);
      }
    };

    if (sdk && buyerId && sellerId) {
      createOrFetchChannel();  // SDK가 준비되었을 때 채널 생성/조회
    }
  }, [sdk, buyerId, sellerId, onChannelSelected]);

  return null;
};

export default ChannelSelector;
