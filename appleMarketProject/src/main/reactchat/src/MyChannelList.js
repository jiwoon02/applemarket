import React, { useEffect, useState } from 'react';
import SendbirdApp from '@sendbird/uikit-react/App';
import { useSendbirdStateContext } from '@sendbird/uikit-react';

const MyChannelList = ({ setChannelData }) => {
  const [channelInfo, setChannelInfo] = useState(null);
  const globalStore = useSendbirdStateContext();
  const sdk = globalStore?.stores?.sdkStore?.sdk;

  useEffect(() => {
    console.log('Fetching data for MyChannelList...'); // Fetch 시작 로그

    fetch('/chatroom/chatdataList')
      .then((response) => response.json())
      .then((data) => {
        console.log('Fetched MyChannelList data:', data);  // Fetch 결과 확인

        // 부모 컴포넌트의 상태 업데이트
        setChannelData({
          appId: data.appId,
          buyerId: data.buyerId,
          buyerNickname: data.buyerNickname,
        });

        // 로컬 상태 업데이트
        setChannelInfo(data);
      })
      .catch((error) => console.error('Error fetching MyChannelList data:', error));
  }, [setChannelData]);

  // 채널 삭제 함수 추가
  const handleDeleteChannel = async (channelUrl) => {
    try {
      const channel = await sdk.groupChannel.getChannel(channelUrl);  // 채널 가져오기
      await channel.delete();  // 채널 삭제
      console.log(`Deleted channel: ${channelUrl}`);

      // 채널 삭제 후 상태 업데이트
      setChannelInfo((prev) => ({
        ...prev,
        channels: prev.channels.filter(channel => channel.url !== channelUrl),
      }));
    } catch (error) {
      console.error('Error deleting channel:', error);
    }
  };

  // 데이터가 로드되기 전에는 로딩 상태를 표시합니다.
  if (!channelInfo) {
    return <p>Loading...</p>;
  }

  return (
    <div style={{width:'70vw', height:'100vh'}}>
      <SendbirdApp 
        appId={channelInfo.appId}
        userId={channelInfo.buyerId}
        nickname={channelInfo.buyerNickname}
      />
    </div>
  );
};

export default MyChannelList;
