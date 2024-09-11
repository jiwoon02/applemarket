import React, { useEffect, useState } from 'react';
import { useSendbirdStateContext } from '@sendbird/uikit-react';
import GroupChannel from '@sendbird/uikit-react/GroupChannel';
import './ChannelSelector.css';
import './App.css';

const ChannelSelector = ({ setChannelData, onChannelSelected }) => {
  const globalStore = useSendbirdStateContext();
  const sdk = globalStore?.stores?.sdkStore?.sdk;
  const [currentChannelUrl, setCurrentChannelUrl] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const urlParams = new URLSearchParams(window.location.search);
        const productID = urlParams.get('productID');
        const response = await fetch(`/chatroom/chatdata?productID=${productID}`);
        const data = await response.json();

        console.log("Fetched data:", data);

        // 이전에 설정된 데이터와 비교해서 업데이트
        setChannelData((prevData) => {
          if (
            prevData.appId === data.appId &&
            prevData.buyerId === data.buyerId &&
            prevData.buyerNickname === data.buyerNickname &&
            prevData.sellerId === data.sellerId &&
            prevData.sellerNickname === data.sellerNickname
          ) {
            return prevData;
          }
          return {
            appId: data.appId,
            buyerId: data.buyerId,
            buyerNickname: data.buyerNickname,
            sellerId: data.sellerId,
            sellerNickname: data.sellerNickname
          };
        });

        // 연결 함수: 이미 연결된 사용자는 연결하지 않음
        const connectUser = async (userId, nickname) => {
          if (sdk?.currentUser?.userId === userId) {
            console.log(`${userId} is already connected`);
            return;
          }

          try {
            await sdk.connect(userId);
            await sdk.updateCurrentUserInfo(nickname, null);
            console.log(`${userId} connected and profile updated`);
          } catch (error) {
            console.error(`Error connecting ${userId}:`, error);
          }
        };

        // Seller와 Buyer 연결
        await connectUser(data.sellerId, data.sellerNickname);
        await connectUser(data.buyerId, data.buyerNickname);

        const createOrFetchChannel = async () => {
          try {
            if (!sdk?.currentUser) {
              throw new Error('SDK currentUser is not initialized');
            }
        
            // invitedUserIds를 정렬하여 순서를 고정
            const invitedUserIds = [data.buyerId, data.sellerId].sort();
        
            const params = {
              invitedUserIds,  // 고정된 순서의 사용자 ID 배열
              name: `${data.buyerId}, ${data.sellerId}`,
              isDistinct: true,
              customType: 'buyer-seller-chat',
            };
            const channel = await sdk.groupChannel.createChannel(params);
            console.log('New or existing channel:', channel.url);
            setCurrentChannelUrl(channel.url);
            onChannelSelected(channel.url);
        
            // Seller 초대
            channel.inviteWithUserIds([data.sellerId], (response, error) => {
              if (error) {
                console.error("Error inviting seller:", error);
                return;
              }
              console.log("Seller invited successfully:", response);
            });
          } catch (error) {
            console.error('Error creating or fetching channel:', error);
          }
        };
        

        // 채널 생성 로직 호출
        await createOrFetchChannel();

      } catch (error) {
        console.error('Error fetching ChannelSelector data:', error);
      }
    };

    // fetchData 함수 호출
    fetchData();

    // 의존성 배열에 sdk와 setChannelData만 추가하여 불필요한 리렌더링 방지
  }, [sdk, setChannelData]);

  return (
    <>
      {currentChannelUrl && (
        <>
          {console.log("currentChannelUrl:", currentChannelUrl)}
          <GroupChannel channelUrl={currentChannelUrl} />
        </>
      )}
    </>
  );
};

export default ChannelSelector;
