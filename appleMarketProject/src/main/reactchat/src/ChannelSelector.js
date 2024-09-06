import { useEffect } from 'react';
import { useSendbirdStateContext } from '@sendbird/uikit-react';

const ChannelSelector = ({ setChannelData, onChannelSelected }) => {
  const globalStore = useSendbirdStateContext();
  const sdk = globalStore?.stores?.sdkStore?.sdk;

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const productID = urlParams.get('productID');

    // Fetch 데이터로 buyer, seller 정보를 가져옵니다.
    fetch(`/chatroom/chatdata?productID=${productID}`)
      .then((response) => response.json())
      .then((data) => {
        console.log("Fetched data:", data);

        // 현재 상태가 변하지 않았을 때는 setChannelData를 실행하지 않음
        setChannelData((prevData) => {
          if (
            prevData.appId === data.appId &&
            prevData.buyerId === data.buyerId &&
            prevData.buyerNickname === data.buyerNickname &&
            prevData.sellerId === data.sellerId
          ) {
            return prevData; // 동일한 데이터일 때 업데이트하지 않음
          }
          return {
            appId: data.appId,
            buyerId: data.buyerId,
            buyerNickname: data.buyerNickname,
            sellerId: data.sellerId,
          };
        });

        const createOrFetchChannel = async () => {
          try {
            if (!sdk?.currentUser) {
              throw new Error('SDK currentUser is not initialized');
            }

            const params = {
              invitedUserIds: [data.buyerId, data.sellerId],  // 초대할 사용자 ID 배열
              name: `${data.sellerId}`,  // 채널 이름
              isDistinct: true,  // 동일한 사용자들로 된 기존 채널이 있으면 재사용
              customType: 'buyer-seller-chat',  // 커스텀 타입 (선택)
            };

            const channel = await sdk.groupChannel.createChannel(params);  // 채널 생성 또는 기존 채널 반환
            console.log('New or existing channel:', channel.url);
            onChannelSelected(channel.url);  // 채널 URL 전달
          } catch (error) {
            console.error('Error creating or fetching channel:', error);
          }
        };

        // 채널 생성 또는 조회는 buyerId, sellerId가 변할 때만 실행
        if (sdk && sdk.groupChannel && data.buyerId && data.sellerId) {
          createOrFetchChannel();  // SDK가 준비되었을 때 채널 생성/조회
        }
      })
      .catch((error) => console.error('Error fetching ChannelSelector data:', error));
  }, [sdk, setChannelData]);  // 의존성 배열에서 onChannelSelected 제거, 반복 실행 방지

  return null;
};

export default ChannelSelector;
